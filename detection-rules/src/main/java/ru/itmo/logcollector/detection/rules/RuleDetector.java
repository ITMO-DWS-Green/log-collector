package ru.itmo.logcollector.detection.rules;

import java.util.List;
import ru.itmo.logcollector.core.model.Detection;
import ru.itmo.logcollector.core.model.DetectionApproach;
import ru.itmo.logcollector.core.model.TelemetryBatch;
import ru.itmo.logcollector.core.port.Detector;

/**
 * Подход «правила и пороги» (FR-05): объединяет правила (#18) и статистические пороги (#19).
 * Пока срабатываний не даёт.
 */
public class RuleDetector implements Detector {

    @Override
    public DetectionApproach approach() {
        return DetectionApproach.RULES;
    }

    @Override
    public List<Detection> detect(TelemetryBatch batch) {
        // TODO #18: правила «условие + длительность» из конфигурации
        // TODO #19: статистические пороги по истории метрики
        return List.of();
    }
}
