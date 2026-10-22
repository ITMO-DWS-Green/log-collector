package ru.itmo.logcollector.core.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Значение метрики в едином формате (FR-04).
 *
 * @param metric имя метрики, см. {@link Metrics}
 */
public record MetricSample(Instant timestamp, ServiceName service, String metric, double value) {
    public MetricSample {
        Objects.requireNonNull(timestamp, "timestamp");
        Objects.requireNonNull(service, "service");
        Objects.requireNonNull(metric, "metric");
    }
}
