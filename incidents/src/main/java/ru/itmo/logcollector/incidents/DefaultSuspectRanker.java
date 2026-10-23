package ru.itmo.logcollector.incidents;

import java.util.List;
import ru.itmo.logcollector.core.model.Incident;
import ru.itmo.logcollector.core.model.SuspectService;
import ru.itmo.logcollector.core.port.SuspectRanker;
import ru.itmo.logcollector.core.port.TelemetryStore;

/** Ранжирование подозрительных сервисов (FR-09, #27). */
public class DefaultSuspectRanker implements SuspectRanker {

    @Override
    public List<SuspectService> rank(Incident incident, TelemetryStore telemetry) {
        // TODO #27: например, кто отклонился раньше и сильнее
        return List.of();
    }
}
