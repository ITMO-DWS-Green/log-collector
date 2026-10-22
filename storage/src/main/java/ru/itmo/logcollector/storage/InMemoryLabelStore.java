package ru.itmo.logcollector.storage;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import ru.itmo.logcollector.core.model.FaultLabel;
import ru.itmo.logcollector.core.port.LabelStore;

/** Разметка в памяти. Заменить вместе с #17, если разметку нужно хранить между перезапусками. */
public class InMemoryLabelStore implements LabelStore {

    private final Map<String, List<FaultLabel>> byScenario = new ConcurrentHashMap<>();

    @Override
    public void save(FaultLabel label) {
        byScenario.computeIfAbsent(label.scenarioId(), k -> new CopyOnWriteArrayList<>()).add(label);
    }

    @Override
    public Set<String> scenarios() {
        return Set.copyOf(byScenario.keySet());
    }

    @Override
    public List<FaultLabel> labels(String scenarioId) {
        return List.copyOf(byScenario.getOrDefault(scenarioId, List.of()));
    }
}
