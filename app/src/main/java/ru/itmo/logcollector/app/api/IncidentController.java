package ru.itmo.logcollector.app.api;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.logcollector.core.model.DetectionApproach;
import ru.itmo.logcollector.core.model.Incident;
import ru.itmo.logcollector.core.port.IncidentStore;

/** Список и карточка инцидента (FR-13). */
@RestController
@RequestMapping("/api/v1/incidents")
public class IncidentController {

    private final IncidentStore incidents;

    public IncidentController(IncidentStore incidents) {
        this.incidents = incidents;
    }

    @GetMapping
    public List<Incident> list(@RequestParam(required = false) DetectionApproach approach) {
        return approach == null ? incidents.findAll() : incidents.findByApproach(approach);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Incident> get(@PathVariable String id) {
        return ResponseEntity.of(incidents.findById(id));
    }
}
