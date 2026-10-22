package ru.itmo.logcollector.detection.ml;

import java.util.List;
import ru.itmo.logcollector.core.model.Detection;
import ru.itmo.logcollector.core.model.DetectionApproach;
import ru.itmo.logcollector.core.model.TelemetryBatch;
import ru.itmo.logcollector.core.port.Detector;

/**
 * Подход «ML» (FR-06). Пока срабатываний не даёт.
 *
 * <p>Если модель будет на Python — этот класс станет HTTP-клиентом к ML-сервису,
 * контракт {@link Detector} при этом не меняется (открытый вопрос в docs/architecture/decisions.md).
 */
public class MlDetector implements Detector {

    @Override
    public DetectionApproach approach() {
        return DetectionApproach.ML;
    }

    @Override
    public List<Detection> detect(TelemetryBatch batch) {
        // TODO #20: признаки из логов и метрик
        // TODO #21: модель и инференс
        return List.of();
    }
}
