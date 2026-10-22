package ru.itmo.logcollector.app.api;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.logcollector.core.model.FaultLabel;
import ru.itmo.logcollector.core.model.QualityReport;
import ru.itmo.logcollector.core.port.IncidentStore;
import ru.itmo.logcollector.core.port.LabelStore;
import ru.itmo.logcollector.core.port.QualityEvaluator;
import ru.itmo.logcollector.core.model.DetectionApproach;

/** Разметка сбоев от генератора (FR-02) и сравнение подходов (FR-07, FR-13). */
@RestController
@RequestMapping("/api/v1")
public class EvaluationController {

    private final LabelStore labels;
    private final IncidentStore incidents;
    private final QualityEvaluator evaluator;

    public EvaluationController(LabelStore labels, IncidentStore incidents, QualityEvaluator evaluator) {
        this.labels = labels;
        this.incidents = incidents;
        this.evaluator = evaluator;
    }

    @PostMapping("/labels")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void addLabel(@RequestBody FaultLabel label) {
        labels.save(label);
    }

    @GetMapping("/scenarios")
    public Set<String> scenarios() {
        return labels.scenarios();
    }

    @GetMapping("/scenarios/{scenarioId}/evaluation")
    public List<QualityReport> evaluate(@PathVariable String scenarioId) {
        List<FaultLabel> scenarioLabels = labels.labels(scenarioId);
        return Arrays.stream(DetectionApproach.values())
                .map(a -> evaluator.evaluate(scenarioId, a, scenarioLabels, incidents.findByApproach(a)))
                .toList();
    }
}
