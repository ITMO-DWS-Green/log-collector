package ru.itmo.logcollector.core.port;

import java.util.List;
import java.util.Optional;
import ru.itmo.logcollector.core.model.DetectionApproach;
import ru.itmo.logcollector.core.model.Incident;

/** Хранилище инцидентов. Сохранение с тем же id заменяет версию. */
public interface IncidentStore {
    void save(Incident incident);

    Optional<Incident> findById(String id);

    /** Все инциденты, новые первыми. */
    List<Incident> findAll();

    List<Incident> findByApproach(DetectionApproach approach);
}
