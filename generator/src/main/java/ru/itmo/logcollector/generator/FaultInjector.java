package ru.itmo.logcollector.generator;

import java.time.Instant;
import org.springframework.stereotype.Component;
import ru.itmo.logcollector.core.model.TelemetryBatch;

/**
 * Внедрение сбоев по сценарию (FR-02, #11) и разметка (#12).
 * Получает «здоровую» пачку и искажает её, если сейчас по сценарию идёт сбой;
 * в момент начала сбоя отправляет разметку через {@link CollectorClient#sendLabel}.
 */
@Component
public class FaultInjector {

    public TelemetryBatch apply(TelemetryBatch healthy, Instant now) {
        // TODO #11: формат сценария, типы сбоев из FaultType, распространение на зависимые сервисы
        // TODO #12: FaultLabel с началом, концом, типом и сервисом-источником
        return healthy;
    }
}
