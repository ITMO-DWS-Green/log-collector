package ru.itmo.logcollector.core.model;

/** Имена метрик, о которых договорились генератор и обнаружение. Новую метрику добавляем сюда. */
public final class Metrics {
    /** Задержка ответа, миллисекунды. */
    public static final String LATENCY_MS = "latency_ms";
    /** Доля ответов с ошибкой, от 0 до 1. */
    public static final String ERROR_RATE = "error_rate";
    /** Число запросов в секунду. */
    public static final String REQUESTS_PER_SECOND = "rps";
    /** Загрузка процессора, от 0 до 1. */
    public static final String CPU_USAGE = "cpu_usage";
    /** Занятая память, мегабайты. */
    public static final String MEMORY_MB = "memory_mb";

    private Metrics() {
    }
}
