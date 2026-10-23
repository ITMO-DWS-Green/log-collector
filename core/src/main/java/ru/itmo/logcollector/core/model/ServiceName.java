package ru.itmo.logcollector.core.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** Имя сервиса, за которым наблюдаем (см. глоссарий: «Сервис»). В JSON — просто строка. */
public record ServiceName(String value) {
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public ServiceName {
        Objects.requireNonNull(value, "value");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Имя сервиса не может быть пустым");
        }
    }

    @Override
    @JsonValue
    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
