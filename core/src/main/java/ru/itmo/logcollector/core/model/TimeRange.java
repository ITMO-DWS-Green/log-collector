package ru.itmo.logcollector.core.model;

import java.time.Instant;
import java.util.Objects;

/** Полуинтервал времени [from, to). */
public record TimeRange(Instant from, Instant to) {
    public TimeRange {
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("to раньше from");
        }
    }

    public boolean contains(Instant t) {
        return !t.isBefore(from) && t.isBefore(to);
    }
}
