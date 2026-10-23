package ru.itmo.logcollector.core.model;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Инцидент — группа связанных срабатываний одного подхода (FR-08).
 * Неизменяемый: каждый этап конвейера возвращает новую версию через {@code with...}.
 *
 * @param team ответственная команда; {@code null}, пока не определена
 */
public record Incident(
        String id,
        DetectionApproach approach,
        IncidentStatus status,
        Instant start,
        Instant end,
        List<Detection> detections,
        List<SuspectService> suspects,
        Explanation explanation,
        Team team) {

    public Incident {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(approach, "approach");
        Objects.requireNonNull(status, "status");
        Objects.requireNonNull(start, "start");
        Objects.requireNonNull(end, "end");
        detections = detections == null ? List.of() : List.copyOf(detections);
        suspects = suspects == null ? List.of() : List.copyOf(suspects);
        explanation = explanation == null ? Explanation.none() : explanation;
    }

    public TimeRange range() {
        return new TimeRange(start, end);
    }

    /** Сервис-источник инцидента — первый в списке подозрительных. */
    public ServiceName sourceService() {
        return suspects.isEmpty() ? null : suspects.get(0).service();
    }

    public Incident withSuspects(List<SuspectService> value) {
        return new Incident(id, approach, status, start, end, detections, value, explanation, team);
    }

    public Incident withExplanation(Explanation value) {
        return new Incident(id, approach, status, start, end, detections, suspects, value, team);
    }

    public Incident withTeam(Team value) {
        return new Incident(id, approach, status, start, end, detections, suspects, explanation, value);
    }
}
