package ru.itmo.logcollector.core.model;

/** Подход к обнаружению аномалий. Сравниваются между собой (FR-07). */
public enum DetectionApproach {
    /** Правила и статистические пороги (FR-05). */
    RULES,
    /** Модель машинного обучения (FR-06). */
    ML
}
