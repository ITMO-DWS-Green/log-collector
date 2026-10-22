package ru.itmo.logcollector.generator;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.itmo.logcollector.core.model.FaultLabel;
import ru.itmo.logcollector.core.model.TelemetryBatch;

/** HTTP-клиент к log-collector. Контракт API — docs/architecture/modules.md. */
@Component
public class CollectorClient {

    private final RestClient http;

    public CollectorClient(RestClient.Builder builder, GeneratorProperties props) {
        this.http = builder.baseUrl(props.targetUrl().toString()).build();
    }

    public void send(TelemetryBatch batch) {
        http.post().uri("/api/v1/telemetry").body(batch).retrieve().toBodilessEntity();
    }

    public void sendLabel(FaultLabel label) {
        http.post().uri("/api/v1/labels").body(label).retrieve().toBodilessEntity();
    }
}
