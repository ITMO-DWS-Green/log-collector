package ru.itmo.logcollector.core.port;

import java.time.Instant;
import java.util.List;
import ru.itmo.logcollector.core.model.Detection;
import ru.itmo.logcollector.core.model.Incident;

/**
 * Объединение срабатываний в инциденты (FR-08). Один экземпляр на один подход.
 * Возвращает только инциденты, которые появились или изменились в этом вызове.
 */
public interface IncidentCorrelator {
    List<Incident> correlate(List<Detection> detections, Instant now);
}
