package ru.itmo.logcollector.core.model;

/** Подозрительный сервис инцидента (FR-09). Чем выше score, тем вероятнее он причина. */
public record SuspectService(ServiceName service, double score) {
}
