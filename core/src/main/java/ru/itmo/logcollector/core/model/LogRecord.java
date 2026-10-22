package ru.itmo.logcollector.core.model;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

/**
 * Запись лога в едином формате (FR-04).
 *
 * @param timestamp  время на общей временной шкале (UTC)
 * @param service    сервис, который написал запись
 * @param level      уровень записи
 * @param message    текст сообщения
 * @param traceId    идентификатор запроса, сквозной между сервисами; может быть {@code null}
 * @param attributes дополнительные поля источника
 */
public record LogRecord(
        Instant timestamp,
        ServiceName service,
        LogLevel level,
        String message,
        String traceId,
        Map<String, String> attributes) {

    public LogRecord {
        Objects.requireNonNull(timestamp, "timestamp");
        Objects.requireNonNull(service, "service");
        Objects.requireNonNull(level, "level");
        message = message == null ? "" : message;
        attributes = attributes == null ? Map.of() : Map.copyOf(attributes);
    }
}
