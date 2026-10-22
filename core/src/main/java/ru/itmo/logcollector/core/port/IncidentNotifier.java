package ru.itmo.logcollector.core.port;

import ru.itmo.logcollector.core.model.Incident;

/** Чат инцидента (FR-12): создать чат, добавить ответственных, отправить описание. */
public interface IncidentNotifier {
    void notifyNew(Incident incident);
}
