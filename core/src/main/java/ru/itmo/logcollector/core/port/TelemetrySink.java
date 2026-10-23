package ru.itmo.logcollector.core.port;

import ru.itmo.logcollector.core.model.TelemetryBatch;

/** Вход конвейера: сюда приём данных отдаёт нормализованные пачки. Реализует модуль app. */
public interface TelemetrySink {
    void accept(TelemetryBatch batch);
}
