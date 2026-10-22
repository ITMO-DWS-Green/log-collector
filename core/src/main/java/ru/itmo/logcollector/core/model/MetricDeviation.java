package ru.itmo.logcollector.core.model;

/** Отклонившаяся метрика в объяснении инцидента (FR-10). */
public record MetricDeviation(ServiceName service, String metric, double expected, double actual) {
}
