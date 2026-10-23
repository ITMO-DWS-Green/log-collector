package ru.itmo.logcollector.generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/** Генератор: имитирует облачную инфраструктуру и шлёт её логи и метрики в log-collector (FR-01, FR-02). */
@SpringBootApplication
@EnableScheduling
@ConfigurationPropertiesScan
public class GeneratorApplication {
    public static void main(String[] args) {
        SpringApplication.run(GeneratorApplication.class, args);
    }
}
