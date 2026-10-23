package ru.itmo.logcollector.response;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import ru.itmo.logcollector.core.model.Incident;
import ru.itmo.logcollector.core.port.IncidentNotifier;

/**
 * Временная замена чата инцидента: пишет в лог (FR-12).
 * Настоящая реализация — по #32 после выбора мессенджера (#31).
 */
public class LoggingIncidentNotifier implements IncidentNotifier {

    private static final Logger LOG = System.getLogger(LoggingIncidentNotifier.class.getName());

    @Override
    public void notifyNew(Incident incident) {
        String team = incident.team() == null ? "не определена" : incident.team().name();
        LOG.log(Level.INFO, "Инцидент {0} ({1}), команда: {2}", incident.id(), incident.approach(), team);
    }
}
