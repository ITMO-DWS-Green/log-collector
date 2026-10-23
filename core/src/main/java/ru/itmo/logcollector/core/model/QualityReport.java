package ru.itmo.logcollector.core.model;

import java.time.Duration;

/**
 * Показатели качества подхода на одном сценарии (FR-07).
 *
 * @param falseAlarmRate     доля инцидентов, не соответствующих ни одному сбою
 * @param meanDetectionDelay среднее время от начала сбоя до первого срабатывания; {@code null}, если ничего не найдено
 */
public record QualityReport(
        String scenarioId,
        DetectionApproach approach,
        int faults,
        int incidents,
        double precision,
        double recall,
        double f1,
        double falseAlarmRate,
        Duration meanDetectionDelay) {
}
