package ru.itmo.logcollector.app.api;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.itmo.logcollector.core.model.LogRecord;
import ru.itmo.logcollector.core.model.MetricSample;
import ru.itmo.logcollector.core.model.ServiceName;
import ru.itmo.logcollector.core.model.TelemetryBatch;
import ru.itmo.logcollector.core.model.TimeRange;
import ru.itmo.logcollector.core.port.TelemetryStore;
import ru.itmo.logcollector.ingestion.FileImporter;
import ru.itmo.logcollector.ingestion.IngestionService;

/** Приём данных (FR-03) и чтение для графиков и логов в интерфейсе (FR-13). */
@RestController
@RequestMapping("/api/v1")
public class TelemetryController {

    private final IngestionService ingestion;
    private final FileImporter importer;
    private final TelemetryStore telemetry;

    public TelemetryController(IngestionService ingestion, FileImporter importer, TelemetryStore telemetry) {
        this.ingestion = ingestion;
        this.importer = importer;
        this.telemetry = telemetry;
    }

    @PostMapping("/telemetry")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void ingest(@RequestBody TelemetryBatch batch) {
        ingestion.ingest(batch);
    }

    @PostMapping("/telemetry/file")
    public ResponseEntity<Map<String, Object>> importFile(@RequestPart("file") MultipartFile file) throws IOException {
        try {
            int accepted = importer.importJsonLines(file.getInputStream());
            return ResponseEntity.ok(Map.of("accepted", accepted));
        } catch (UnsupportedOperationException e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/services")
    public Set<ServiceName> services() {
        return telemetry.services();
    }

    @GetMapping("/services/{service}/metrics/{metric}")
    public List<MetricSample> metrics(@PathVariable String service, @PathVariable String metric,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
        return telemetry.metrics(new ServiceName(service), metric, new TimeRange(from, to));
    }

    @GetMapping("/services/{service}/logs")
    public List<LogRecord> logs(@PathVariable String service,
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
        return telemetry.logs(new ServiceName(service), new TimeRange(from, to));
    }
}
