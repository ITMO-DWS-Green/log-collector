package ru.itmo.logcollector.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

/** Пачка данных, которая идёт по конвейеру: записи лога и значения метрик. */
public record TelemetryBatch(List<LogRecord> logs, List<MetricSample> metrics) {
    public TelemetryBatch {
        logs = logs == null ? List.of() : List.copyOf(logs);
        metrics = metrics == null ? List.of() : List.copyOf(metrics);
    }

    public static TelemetryBatch empty() {
        return new TelemetryBatch(List.of(), List.of());
    }

    @JsonIgnore
    public boolean isEmpty() {
        return logs.isEmpty() && metrics.isEmpty();
    }
}
