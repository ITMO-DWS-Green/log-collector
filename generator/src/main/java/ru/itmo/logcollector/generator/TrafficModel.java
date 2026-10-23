package ru.itmo.logcollector.generator;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.random.RandomGenerator;
import org.springframework.stereotype.Component;
import ru.itmo.logcollector.core.model.LogLevel;
import ru.itmo.logcollector.core.model.LogRecord;
import ru.itmo.logcollector.core.model.MetricSample;
import ru.itmo.logcollector.core.model.Metrics;
import ru.itmo.logcollector.core.model.ServiceName;
import ru.itmo.logcollector.core.model.TelemetryBatch;

/**
 * Нормальное поведение сервисов: что они пишут в лог и какие у них метрики.
 * Сейчас — минимальная заглушка, чтобы данные шли по конвейеру.
 */
@Component
public class TrafficModel {

    private final GeneratorProperties props;
    private final RandomGenerator random = RandomGenerator.getDefault();

    public TrafficModel(GeneratorProperties props) {
        this.props = props;
    }

    public TelemetryBatch next(Instant now) {
        List<LogRecord> logs = new ArrayList<>();
        List<MetricSample> metrics = new ArrayList<>();
        for (GeneratorProperties.ServiceSpec spec : props.services()) {
            ServiceName service = new ServiceName(spec.name());
            // TODO #9: правдоподобные логи — фон INFO, редкие WARN/ERROR, сквозной traceId между сервисами
            logs.add(new LogRecord(now, service, LogLevel.INFO, "request handled",
                    UUID.randomUUID().toString(), Map.of()));
            // TODO #10: метрики с суточными колебаниями; зависимость от вызываемых сервисов (#8)
            metrics.add(new MetricSample(now, service, Metrics.LATENCY_MS, 50 + random.nextGaussian() * 5));
            metrics.add(new MetricSample(now, service, Metrics.ERROR_RATE, Math.max(0, 0.01 + random.nextGaussian() * 0.002)));
            metrics.add(new MetricSample(now, service, Metrics.REQUESTS_PER_SECOND, 100 + random.nextGaussian() * 10));
        }
        return new TelemetryBatch(logs, metrics);
    }
}
