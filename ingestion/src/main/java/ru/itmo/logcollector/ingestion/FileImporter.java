package ru.itmo.logcollector.ingestion;

import java.io.IOException;
import java.io.InputStream;

/**
 * Загрузка данных из файла для офлайн-анализа (FR-03, #15).
 * Формат файла — JSON Lines, описан в docs/architecture/modules.md.
 */
public class FileImporter {

    private final IngestionService ingestion;

    public FileImporter(IngestionService ingestion) {
        this.ingestion = ingestion;
    }

    /** @return сколько записей принято */
    public int importJsonLines(InputStream in) throws IOException {
        // TODO #15: читать построчно, собирать пачки и отдавать в ingestion.ingest(...)
        throw new UnsupportedOperationException("Загрузка из файла ещё не реализована (#15)");
    }
}
