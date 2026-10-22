package ru.itmo.logcollector.core.model;

/** Тип сбоя, который генератор внедряет по сценарию (FR-02). */
public enum FaultType {
    /** Рост задержки ответа. */
    LATENCY_SPIKE,
    /** Всплеск ошибок. */
    ERROR_BURST,
    /** Сервис перестал отвечать. */
    SERVICE_DOWN,
    /** Утечка ресурса: память или соединения растут, пока сервис не деградирует. */
    RESOURCE_LEAK
}
