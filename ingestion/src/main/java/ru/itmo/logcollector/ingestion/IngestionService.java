package ru.itmo.logcollector.ingestion;

import ru.itmo.logcollector.core.model.TelemetryBatch;
import ru.itmo.logcollector.core.port.TelemetrySink;

/**
 * Приём данных (FR-03): поток и файл приходят сюда и дальше идут одним путём.
 * Нормализует пачку (FR-04) и передаёт её в конвейер.
 */
public class IngestionService {

    private final TelemetrySink sink;

    public IngestionService(TelemetrySink sink) {
        this.sink = sink;
    }

    public void ingest(TelemetryBatch batch) {
        if (batch.isEmpty()) {
            return;
        }
        sink.accept(normalize(batch));
    }

    TelemetryBatch normalize(TelemetryBatch batch) {
        // TODO #16: единая временная шкала (UTC, округление), отбраковка некорректных записей
        return batch;
    }
}
