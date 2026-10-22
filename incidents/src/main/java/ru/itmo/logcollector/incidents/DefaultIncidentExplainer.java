package ru.itmo.logcollector.incidents;

import ru.itmo.logcollector.core.model.Explanation;
import ru.itmo.logcollector.core.model.Incident;
import ru.itmo.logcollector.core.port.IncidentExplainer;
import ru.itmo.logcollector.core.port.TelemetryStore;

/** Объяснение инцидента (FR-10, #28). */
public class DefaultIncidentExplainer implements IncidentExplainer {

    @Override
    public Explanation explain(Incident incident, TelemetryStore telemetry) {
        // TODO #28: отклонившиеся метрики относительно нормы + характерные записи лога
        return Explanation.none();
    }
}
