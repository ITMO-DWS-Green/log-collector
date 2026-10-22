package ru.itmo.logcollector.generator;

import java.time.Clock;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

/** Каждый тик: нормальный трафик → сбои по сценарию → отправка в log-collector. */
@Component
public class Emitter {

    private static final Logger LOG = LoggerFactory.getLogger(Emitter.class);

    private final TrafficModel traffic;
    private final FaultInjector faults;
    private final CollectorClient client;
    private final Clock clock = Clock.systemUTC();

    public Emitter(TrafficModel traffic, FaultInjector faults, CollectorClient client) {
        this.traffic = traffic;
        this.faults = faults;
        this.client = client;
    }

    @Scheduled(fixedRateString = "${generator.tick-ms:1000}")
    public void tick() {
        Instant now = clock.instant();
        try {
            client.send(faults.apply(traffic.next(now), now));
        } catch (RestClientException e) {
            LOG.warn("log-collector недоступен: {}", e.getMessage());
        }
    }
}
