package ru.itmo.logcollector.generator;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = "generator.tick-ms=3600000")
class GeneratorApplicationTest {

    @Autowired
    TrafficModel traffic;

    @Test
    void producesDataForEveryConfiguredService() {
        var batch = traffic.next(Instant.parse("2026-10-23T10:00:00Z"));
        assertThat(batch.logs()).extracting(l -> l.service().value())
                .containsExactlyInAnyOrder("gateway", "orders", "payments", "database");
        assertThat(batch.metrics()).isNotEmpty();
    }
}
