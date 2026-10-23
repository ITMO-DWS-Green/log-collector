package ru.itmo.logcollector.core.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Срабатывание: подход заметил аномалию (не «алерт» и не «инцидент»).
 *
 * @param signal что именно отклонилось: имя метрики, правила или признака модели
 * @param score  сила отклонения; чем больше, тем увереннее подход
 */
public record Detection(
        String id,
        Instant timestamp,
        ServiceName service,
        DetectionApproach approach,
        String signal,
        double score) {

    public Detection {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(timestamp, "timestamp");
        Objects.requireNonNull(service, "service");
        Objects.requireNonNull(approach, "approach");
        Objects.requireNonNull(signal, "signal");
    }
}
