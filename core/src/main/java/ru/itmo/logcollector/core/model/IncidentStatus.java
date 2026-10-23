package ru.itmo.logcollector.core.model;

/** Жизненный цикл инцидента. */
public enum IncidentStatus {
    /** Срабатывания продолжают приходить. */
    OPEN,
    /** Новых срабатываний нет дольше окна объединения. */
    RESOLVED
}
