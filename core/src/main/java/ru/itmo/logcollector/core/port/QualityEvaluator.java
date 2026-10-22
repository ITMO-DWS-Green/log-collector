package ru.itmo.logcollector.core.port;

import java.util.List;
import ru.itmo.logcollector.core.model.DetectionApproach;
import ru.itmo.logcollector.core.model.FaultLabel;
import ru.itmo.logcollector.core.model.Incident;
import ru.itmo.logcollector.core.model.QualityReport;

/** Сравнение подходов по разметке (FR-07). */
public interface QualityEvaluator {
    QualityReport evaluate(String scenarioId, DetectionApproach approach,
                           List<FaultLabel> labels, List<Incident> incidents);
}
