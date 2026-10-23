package ru.itmo.logcollector.incidents;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import ru.itmo.logcollector.core.model.Detection;
import ru.itmo.logcollector.core.model.DetectionApproach;
import ru.itmo.logcollector.core.model.Incident;
import ru.itmo.logcollector.core.port.IncidentCorrelator;

/**
 * Объединение срабатываний одного подхода в инциденты (FR-08, #23).
 * Идея: срабатывания, близкие по времени ({@code window}) и по связанным сервисам, — один инцидент;
 * инцидент закрывается, если новых срабатываний нет дольше окна.
 */
public class TimeWindowCorrelator implements IncidentCorrelator {

    private final DetectionApproach approach;
    private final Duration window;

    public TimeWindowCorrelator(DetectionApproach approach, Duration window) {
        this.approach = approach;
        this.window = window;
    }

    @Override
    public List<Incident> correlate(List<Detection> detections, Instant now) {
        // TODO #23: группировать срабатывания this.approach в инциденты по окну this.window
        return List.of();
    }
}
