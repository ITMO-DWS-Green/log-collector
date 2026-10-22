package ru.itmo.logcollector.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class LogCollectorApplication {
    public static void main(String[] args) {
        SpringApplication.run(LogCollectorApplication.class, args);
    }
}
