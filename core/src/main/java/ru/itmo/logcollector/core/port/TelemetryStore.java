package ru.itmo.logcollector.core.port;

import java.util.List;
import java.util.Set;
import ru.itmo.logcollector.core.model.LogRecord;
import ru.itmo.logcollector.core.model.MetricSample;
import ru.itmo.logcollector.core.model.ServiceName;
import ru.itmo.logcollector.core.model.TelemetryBatch;
import ru.itmo.logcollector.core.model.TimeRange;

/** Хранилище логов и метрик (#17). Чтение — для графиков, объяснения и ранжирования. */
public interface TelemetryStore {
    void save(TelemetryBatch batch);

    Set<ServiceName> services();

    /** Записи лога сервиса за интервал, по возрастанию времени. */
    List<LogRecord> logs(ServiceName service, TimeRange range);

    /** Значения метрики сервиса за интервал, по возрастанию времени. */
    List<MetricSample> metrics(ServiceName service, String metric, TimeRange range);
}
