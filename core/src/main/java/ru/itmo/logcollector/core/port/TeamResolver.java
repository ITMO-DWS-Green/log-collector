package ru.itmo.logcollector.core.port;

import java.util.Optional;
import ru.itmo.logcollector.core.model.ServiceName;
import ru.itmo.logcollector.core.model.Team;

/** Справочник «сервис — команда» (FR-11). */
public interface TeamResolver {
    Optional<Team> teamOf(ServiceName service);
}
