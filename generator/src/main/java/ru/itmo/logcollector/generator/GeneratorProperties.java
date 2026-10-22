package ru.itmo.logcollector.generator;

import java.net.URI;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * @param targetUrl адрес log-collector
 * @param services  имитируемые сервисы и кого они вызывают (#8)
 */
@ConfigurationProperties("generator")
public record GeneratorProperties(
        @DefaultValue("http://localhost:8080") URI targetUrl,
        @DefaultValue List<ServiceSpec> services) {

    /** @param calls сервисы, которые вызывает этот сервис */
    public record ServiceSpec(String name, List<String> calls) {
        public ServiceSpec {
            calls = calls == null ? List.of() : calls;
        }
    }
}
