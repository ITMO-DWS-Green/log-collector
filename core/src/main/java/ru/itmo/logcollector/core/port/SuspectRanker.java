package ru.itmo.logcollector.core.port;

import java.util.List;
import ru.itmo.logcollector.core.model.Incident;
import ru.itmo.logcollector.core.model.SuspectService;

/** Ранжирование подозрительных сервисов инцидента (FR-09), самый вероятный первым. */
public interface SuspectRanker {
    List<SuspectService> rank(Incident incident, TelemetryStore telemetry);
}
