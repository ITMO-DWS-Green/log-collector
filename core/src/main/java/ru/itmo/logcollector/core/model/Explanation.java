package ru.itmo.logcollector.core.model;

import java.util.List;

/** Объяснение инцидента: что отклонилось и характерные записи лога (FR-10). Гипотеза, а не диагноз. */
public record Explanation(String summary, List<MetricDeviation> deviations, List<LogRecord> sampleLogs) {
    public Explanation {
        summary = summary == null ? "" : summary;
        deviations = deviations == null ? List.of() : List.copyOf(deviations);
        sampleLogs = sampleLogs == null ? List.of() : List.copyOf(sampleLogs);
    }

    public static Explanation none() {
        return new Explanation("", List.of(), List.of());
    }
}
