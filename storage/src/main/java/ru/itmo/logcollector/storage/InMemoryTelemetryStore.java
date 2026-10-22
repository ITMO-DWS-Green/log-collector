package ru.itmo.logcollector.storage;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import ru.itmo.logcollector.core.model.LogRecord;
import ru.itmo.logcollector.core.model.MetricSample;
import ru.itmo.logcollector.core.model.ServiceName;
import ru.itmo.logcollector.core.model.TelemetryBatch;
import ru.itmo.logcollector.core.model.TimeRange;
import ru.itmo.logcollector.core.port.TelemetryStore;

/**
 * Хранилище в памяти — чтобы конвейер работал с первого дня.
 * Будет заменено настоящим хранилищем по задаче #17; контракт — {@link TelemetryStore}.
 * Срок хранения не соблюдает: данные живут до перезапуска.
 */
public class InMemoryTelemetryStore implements TelemetryStore {

    private final Map<ServiceName, NavigableMap<Instant, List<LogRecord>>> logs = new ConcurrentHashMap<>();
    private final Map<String, NavigableMap<Instant, List<MetricSample>>> metrics = new ConcurrentHashMap<>();
    private final Set<ServiceName> services = ConcurrentHashMap.newKeySet();

    @Override
    public synchronized void save(TelemetryBatch batch) {
        for (LogRecord r : batch.logs()) {
            services.add(r.service());
            put(logs.computeIfAbsent(r.service(), k -> new TreeMap<>()), r.timestamp(), r);
        }
        for (MetricSample s : batch.metrics()) {
            services.add(s.service());
            put(metrics.computeIfAbsent(key(s.service(), s.metric()), k -> new TreeMap<>()), s.timestamp(), s);
        }
    }

    @Override
    public Set<ServiceName> services() {
        return Set.copyOf(services);
    }

    @Override
    public synchronized List<LogRecord> logs(ServiceName service, TimeRange range) {
        return slice(logs.get(service), range);
    }

    @Override
    public synchronized List<MetricSample> metrics(ServiceName service, String metric, TimeRange range) {
        return slice(metrics.get(key(service, metric)), range);
    }

    private static <T> void put(NavigableMap<Instant, List<T>> index, Instant t, T value) {
        index.computeIfAbsent(t, k -> new ArrayList<>()).add(value);
    }

    private static <T> List<T> slice(NavigableMap<Instant, List<T>> index, TimeRange range) {
        if (index == null) {
            return List.of();
        }
        return index.subMap(range.from(), true, range.to(), false).values().stream()
                .flatMap(List::stream)
                .toList();
    }

    private static String key(ServiceName service, String metric) {
        return service.value() + '\u0000' + metric;
    }
}
