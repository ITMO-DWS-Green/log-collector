package ru.itmo.logcollector.response;

import java.util.Map;
import java.util.Optional;
import ru.itmo.logcollector.core.model.ServiceName;
import ru.itmo.logcollector.core.model.Team;
import ru.itmo.logcollector.core.port.TeamResolver;

/** Справочник «сервис — команда» из конфигурации (FR-11, #29, #30). */
public class StaticTeamResolver implements TeamResolver {

    private final Map<String, Team> teamByService;
    private final Team fallback;

    /**
     * @param teamByService имя сервиса → команда
     * @param fallback      команда для сервиса, которого нет в справочнике; может быть {@code null}
     */
    public StaticTeamResolver(Map<String, Team> teamByService, Team fallback) {
        this.teamByService = Map.copyOf(teamByService);
        this.fallback = fallback;
    }

    @Override
    public Optional<Team> teamOf(ServiceName service) {
        if (service == null) {
            return Optional.ofNullable(fallback);
        }
        return Optional.ofNullable(teamByService.getOrDefault(service.value(), fallback));
    }
}
