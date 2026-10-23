package ru.itmo.logcollector.core.port;

import ru.itmo.logcollector.core.model.Explanation;
import ru.itmo.logcollector.core.model.Incident;

/** Объяснение инцидента (FR-10): отклонившиеся метрики и примеры записей лога. */
public interface IncidentExplainer {
    Explanation explain(Incident incident, TelemetryStore telemetry);
}
