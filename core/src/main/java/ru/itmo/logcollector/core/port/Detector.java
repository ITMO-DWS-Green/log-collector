package ru.itmo.logcollector.core.port;

import java.util.List;
import ru.itmo.logcollector.core.model.Detection;
import ru.itmo.logcollector.core.model.DetectionApproach;
import ru.itmo.logcollector.core.model.TelemetryBatch;

/**
 * Обнаружение аномалий (FR-05, FR-06). Оба подхода получают одни и те же пачки.
 * Реализация может хранить состояние между вызовами (скользящие окна, история для порогов).
 * Вызывается из одного потока, потокобезопасность не нужна.
 */
public interface Detector {
    DetectionApproach approach();

    List<Detection> detect(TelemetryBatch batch);
}
