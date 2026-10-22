package ru.itmo.logcollector.storage;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import ru.itmo.logcollector.core.model.DetectionApproach;
import ru.itmo.logcollector.core.model.Incident;
import ru.itmo.logcollector.core.port.IncidentStore;

/** Инциденты в памяти. */
public class InMemoryIncidentStore implements IncidentStore {

    private static final Comparator<Incident> NEWEST_FIRST = Comparator.comparing(Incident::start).reversed();

    private final Map<String, Incident> byId = new ConcurrentHashMap<>();

    @Override
    public void save(Incident incident) {
        byId.put(incident.id(), incident);
    }

    @Override
    public Optional<Incident> findById(String id) {
        return Optional.ofNullable(byId.get(id));
    }

    @Override
    public List<Incident> findAll() {
        return byId.values().stream().sorted(NEWEST_FIRST).toList();
    }

    @Override
    public List<Incident> findByApproach(DetectionApproach approach) {
        return byId.values().stream().filter(i -> i.approach() == approach).sorted(NEWEST_FIRST).toList();
    }
}
