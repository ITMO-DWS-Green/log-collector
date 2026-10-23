package ru.itmo.logcollector.app;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/** Сквозная проверка: JSON-контракт приёма данных и чтения (docs/architecture/modules.md). */
@SpringBootTest
@AutoConfigureMockMvc
class ApiTest {

    @Autowired
    MockMvc mvc;

    @Test
    void acceptsTelemetryAndReturnsItBack() throws Exception {
        String batch = """
                {
                  "logs": [
                    {"timestamp": "2026-10-23T10:00:00Z", "service": "orders", "level": "ERROR",
                     "message": "payment timeout", "traceId": "t-1"}
                  ],
                  "metrics": [
                    {"timestamp": "2026-10-23T10:00:00Z", "service": "orders", "metric": "latency_ms", "value": 950.0}
                  ]
                }
                """;

        mvc.perform(post("/api/v1/telemetry").contentType(MediaType.APPLICATION_JSON).content(batch))
                .andExpect(status().isAccepted());

        mvc.perform(get("/api/v1/services"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("orders"));

        mvc.perform(get("/api/v1/services/orders/metrics/latency_ms")
                        .param("from", "2026-10-23T09:59:00Z").param("to", "2026-10-23T10:01:00Z"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].value").value(950.0));

        mvc.perform(get("/api/v1/services/orders/logs")
                        .param("from", "2026-10-23T09:59:00Z").param("to", "2026-10-23T10:01:00Z"))
                .andExpect(jsonPath("$[0].level").value("ERROR"));

        mvc.perform(get("/api/v1/incidents")).andExpect(status().isOk());
    }

    @Test
    void acceptsFaultLabel() throws Exception {
        String label = """
                {"scenarioId": "s1", "faultId": "f1", "type": "LATENCY_SPIKE", "source": "database",
                 "start": "2026-10-23T10:00:00Z", "end": "2026-10-23T10:05:00Z"}
                """;
        mvc.perform(post("/api/v1/labels").contentType(MediaType.APPLICATION_JSON).content(label))
                .andExpect(status().isAccepted());
        mvc.perform(get("/api/v1/scenarios/s1/evaluation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}
