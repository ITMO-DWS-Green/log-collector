package ru.itmo.logcollector.core.port;

import java.util.List;
import java.util.Set;
import ru.itmo.logcollector.core.model.FaultLabel;

/** Хранилище разметки сбоев (FR-02). Читает только сравнение подходов. */
public interface LabelStore {
    void save(FaultLabel label);

    Set<String> scenarios();

    List<FaultLabel> labels(String scenarioId);
}
