package ru.itmo.logcollector.storage;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import ru.itmo.logcollector.core.model.LogLevel;
import ru.itmo.logcollector.core.model.LogRecord;
import ru.itmo.logcollector.core.model.MetricSample;
import ru.itmo.logcollector.core.model.ServiceName;
import ru.itmo.logcollector.core.model.TelemetryBatch;
import ru.itmo.logcollector.core.model.TimeRange;

class InMemoryTelemetryStoreTest {

    private static final ServiceName ORDERS = new ServiceName("orders");
    private static final Instant T0 = Instant.parse("2026-10-23T10:00:00Z");

    private final InMemoryTelemetryStore store = new InMemoryTelemetryStore();

    @Test
    void returnsOnlyRequestedServiceMetricAndRange() {
        store.save(new TelemetryBatch(
                List.of(new LogRecord(T0, ORDERS, LogLevel.ERROR, "boom", null, Map.of())),
                List.of(
                        new MetricSample(T0, ORDERS, "latency_ms", 50),
                        new MetricSample(T0.plusSeconds(10), ORDERS, "latency_ms", 60),
                        new MetricSample(T0.plusSeconds(20), ORDERS, "latency_ms", 70),
                        new MetricSample(T0, ORDERS, "error_rate", 0.1),
                        new MetricSample(T0, new ServiceName("gateway"), "latency_ms", 5))));

        var range = new TimeRange(T0, T0.plusSeconds(20)); // правая граница не входит

        assertThat(store.metrics(ORDERS, "latency_ms", range))
                .extracting(MetricSample::value)
                .containsExactly(50.0, 60.0);
        assertThat(store.logs(ORDERS, range)).hasSize(1);
        assertThat(store.services()).containsExactlyInAnyOrder(ORDERS, new ServiceName("gateway"));
    }

    @Test
    void unknownServiceGivesEmptyResult() {
        assertThat(store.metrics(ORDERS, "latency_ms", new TimeRange(T0, T0.plusSeconds(1)))).isEmpty();
    }
}
