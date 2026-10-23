package ru.itmo.logcollector.app.pipeline;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import ru.itmo.logcollector.core.model.Detection;
import ru.itmo.logcollector.core.model.DetectionApproach;
import ru.itmo.logcollector.core.model.Explanation;
import ru.itmo.logcollector.core.model.Incident;
import ru.itmo.logcollector.core.model.IncidentStatus;
import ru.itmo.logcollector.core.model.MetricSample;
import ru.itmo.logcollector.core.model.ServiceName;
import ru.itmo.logcollector.core.model.SuspectService;
import ru.itmo.logcollector.core.model.Team;
import ru.itmo.logcollector.core.model.TelemetryBatch;
import ru.itmo.logcollector.core.port.Detector;
import ru.itmo.logcollector.core.port.IncidentCorrelator;
import ru.itmo.logcollector.storage.InMemoryIncidentStore;
import ru.itmo.logcollector.storage.InMemoryTelemetryStore;

/** Проверяет порядок шагов конвейера на поддельных реализациях портов — пример, как тестировать свой модуль. */
class PipelineTest {

    private static final Instant NOW = Instant.parse("2026-10-23T10:00:00Z");
    private static final ServiceName DB = new ServiceName("database");
    private static final Team STORAGE = new Team("storage", "Хранилище", List.of());

    @Test
    void newIncidentIsRankedExplainedAssignedSavedAndNotifiedOnce() {
        var telemetry = new InMemoryTelemetryStore();
        var incidents = new InMemoryIncidentStore();
        List<Incident> notified = new ArrayList<>();

        Detector detector = new Detector() {
            public DetectionApproach approach() {
                return DetectionApproach.RULES;
            }

            public List<Detection> detect(TelemetryBatch batch) {
                return List.of(new Detection("d1", NOW, DB, DetectionApproach.RULES, "latency_ms", 3.0));
            }
        };
        IncidentCorrelator correlator = (detections, now) -> List.of(new Incident(
                "i1", DetectionApproach.RULES, IncidentStatus.OPEN, NOW, NOW, detections, null, null, null));

        var pipeline = new Pipeline(
                telemetry,
                List.of(detector),
                Map.of(DetectionApproach.RULES, correlator),
                (incident, store) -> List.of(new SuspectService(DB, 1.0)),
                (incident, store) -> new Explanation("задержка БД выросла", List.of(), List.of()),
                service -> DB.equals(service) ? Optional.of(STORAGE) : Optional.empty(),
                incidents,
                notified::add,
                Clock.fixed(NOW, ZoneOffset.UTC));

        var batch = new TelemetryBatch(List.of(), List.of(new MetricSample(NOW, DB, "latency_ms", 900)));
        pipeline.accept(batch);
        pipeline.accept(batch); // тот же инцидент обновился — повторного оповещения нет

        Incident saved = incidents.findById("i1").orElseThrow();
        assertThat(saved.sourceService()).isEqualTo(DB);
        assertThat(saved.team()).isEqualTo(STORAGE);
        assertThat(saved.explanation().summary()).isEqualTo("задержка БД выросла");
        assertThat(notified).hasSize(1);
        assertThat(telemetry.services()).containsExactly(DB);
    }
}
