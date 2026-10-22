package ru.itmo.logcollector.app.pipeline;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import ru.itmo.logcollector.core.model.Detection;
import ru.itmo.logcollector.core.model.DetectionApproach;
import ru.itmo.logcollector.core.model.Incident;
import ru.itmo.logcollector.core.model.TelemetryBatch;
import ru.itmo.logcollector.core.port.Detector;
import ru.itmo.logcollector.core.port.IncidentCorrelator;
import ru.itmo.logcollector.core.port.IncidentExplainer;
import ru.itmo.logcollector.core.port.IncidentNotifier;
import ru.itmo.logcollector.core.port.IncidentStore;
import ru.itmo.logcollector.core.port.SuspectRanker;
import ru.itmo.logcollector.core.port.TeamResolver;
import ru.itmo.logcollector.core.port.TelemetrySink;
import ru.itmo.logcollector.core.port.TelemetryStore;

/**
 * Конвейер: пачка → хранилище → каждый подход → инциденты → ранжирование, объяснение,
 * команда → сохранение → оповещение о новом инциденте.
 *
 * <p>Здесь нет логики обнаружения — только порядок шагов. Каждый шаг — порт из core,
 * реализацию даёт свой модуль, поэтому модули можно делать параллельно.
 * Обработка последовательная: детекторам и корреляторам не нужна потокобезопасность.
 */
public class Pipeline implements TelemetrySink {

    private final TelemetryStore telemetry;
    private final List<Detector> detectors;
    private final Map<DetectionApproach, IncidentCorrelator> correlators;
    private final SuspectRanker ranker;
    private final IncidentExplainer explainer;
    private final TeamResolver teams;
    private final IncidentStore incidents;
    private final IncidentNotifier notifier;
    private final Clock clock;

    public Pipeline(TelemetryStore telemetry,
                    List<Detector> detectors,
                    Map<DetectionApproach, IncidentCorrelator> correlators,
                    SuspectRanker ranker,
                    IncidentExplainer explainer,
                    TeamResolver teams,
                    IncidentStore incidents,
                    IncidentNotifier notifier,
                    Clock clock) {
        this.telemetry = telemetry;
        this.detectors = List.copyOf(detectors);
        this.correlators = Map.copyOf(correlators);
        this.ranker = ranker;
        this.explainer = explainer;
        this.teams = teams;
        this.incidents = incidents;
        this.notifier = notifier;
        this.clock = clock;
    }

    @Override
    public synchronized void accept(TelemetryBatch batch) {
        telemetry.save(batch);
        Instant now = clock.instant();
        for (Detector detector : detectors) {
            List<Detection> detections = detector.detect(batch);
            IncidentCorrelator correlator = correlators.get(detector.approach());
            if (correlator == null) {
                throw new IllegalStateException("Нет коррелятора для подхода " + detector.approach());
            }
            for (Incident changed : correlator.correlate(detections, now)) {
                handle(changed);
            }
        }
    }

    private void handle(Incident changed) {
        boolean isNew = incidents.findById(changed.id()).isEmpty();
        Incident ranked = changed.withSuspects(ranker.rank(changed, telemetry));
        Incident enriched = ranked
                .withExplanation(explainer.explain(ranked, telemetry))
                .withTeam(teams.teamOf(ranked.sourceService()).orElse(null));
        incidents.save(enriched);
        if (isNew) {
            // Открытый вопрос: оповещать по обоим подходам или только по одному (см. open-questions).
            notifier.notifyNew(enriched);
        }
    }
}
