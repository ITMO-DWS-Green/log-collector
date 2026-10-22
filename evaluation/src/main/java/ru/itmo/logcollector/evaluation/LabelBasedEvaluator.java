package ru.itmo.logcollector.evaluation;

import java.util.List;
import ru.itmo.logcollector.core.model.DetectionApproach;
import ru.itmo.logcollector.core.model.FaultLabel;
import ru.itmo.logcollector.core.model.Incident;
import ru.itmo.logcollector.core.model.QualityReport;
import ru.itmo.logcollector.core.port.QualityEvaluator;

/**
 * Показатели качества по разметке (FR-07, #25).
 * Сначала договориться, когда инцидент «попал» в сбой (пересечение по времени? тот же сервис-источник?),
 * и записать это в docs/requirements.
 */
public class LabelBasedEvaluator implements QualityEvaluator {

    @Override
    public QualityReport evaluate(String scenarioId, DetectionApproach approach,
                                  List<FaultLabel> labels, List<Incident> incidents) {
        // TODO #25: precision, recall, F1, доля ложных тревог, задержка обнаружения
        return new QualityReport(scenarioId, approach, labels.size(), incidents.size(), 0, 0, 0, 0, null);
    }
}
