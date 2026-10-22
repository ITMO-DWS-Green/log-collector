package ru.itmo.logcollector.app.config;

import java.time.Duration;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Настройки из application.yml, префикс {@code logcollector}.
 *
 * @param correlationWindow окно объединения срабатываний в инцидент
 * @param teams             справочник «сервис — команда»
 * @param fallbackTeam      id команды для сервисов, которых нет в справочнике
 */
@ConfigurationProperties("logcollector")
public record LogCollectorProperties(
        @DefaultValue("2m") Duration correlationWindow,
        @DefaultValue List<TeamProperties> teams,
        String fallbackTeam) {

    public record TeamProperties(String id, String name, List<String> members, List<String> services) {
        public TeamProperties {
            members = members == null ? List.of() : members;
            services = services == null ? List.of() : services;
        }
    }
}
