package ru.itmo.logcollector.core.model;

import java.util.List;

/**
 * Команда, отвечающая за сервисы (FR-11). Не наша команда разработки.
 *
 * @param members идентификаторы участников в мессенджере
 */
public record Team(String id, String name, List<String> members) {
    public Team {
        members = members == null ? List.of() : List.copyOf(members);
    }
}
