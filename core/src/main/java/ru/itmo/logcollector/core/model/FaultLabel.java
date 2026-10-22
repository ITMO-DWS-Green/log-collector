package ru.itmo.logcollector.core.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Разметка внедрённого сбоя — эталон для оценки качества (FR-02, FR-07).
 * Видна только сравнению подходов, в обнаружение не попадает.
 *
 * @param source сервис-источник: где сбой начался
 */
public record FaultLabel(
        String scenarioId,
        String faultId,
        FaultType type,
        ServiceName source,
        Instant start,
        Instant end) {

    public FaultLabel {
        Objects.requireNonNull(scenarioId, "scenarioId");
        Objects.requireNonNull(faultId, "faultId");
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(start, "start");
        Objects.requireNonNull(end, "end");
    }

    public TimeRange range() {
        return new TimeRange(start, end);
    }
}
