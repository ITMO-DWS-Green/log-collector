package ru.itmo.logcollector.app.config;

import java.time.Clock;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.itmo.logcollector.app.pipeline.Pipeline;
import ru.itmo.logcollector.core.model.DetectionApproach;
import ru.itmo.logcollector.core.model.Team;
import ru.itmo.logcollector.core.port.Detector;
import ru.itmo.logcollector.core.port.IncidentCorrelator;
import ru.itmo.logcollector.core.port.IncidentExplainer;
import ru.itmo.logcollector.core.port.IncidentNotifier;
import ru.itmo.logcollector.core.port.IncidentStore;
import ru.itmo.logcollector.core.port.LabelStore;
import ru.itmo.logcollector.core.port.QualityEvaluator;
import ru.itmo.logcollector.core.port.SuspectRanker;
import ru.itmo.logcollector.core.port.TeamResolver;
import ru.itmo.logcollector.core.port.TelemetryStore;
import ru.itmo.logcollector.detection.ml.MlDetector;
import ru.itmo.logcollector.detection.rules.RuleDetector;
import ru.itmo.logcollector.evaluation.LabelBasedEvaluator;
import ru.itmo.logcollector.incidents.DefaultIncidentExplainer;
import ru.itmo.logcollector.incidents.DefaultSuspectRanker;
import ru.itmo.logcollector.incidents.TimeWindowCorrelator;
import ru.itmo.logcollector.ingestion.FileImporter;
import ru.itmo.logcollector.ingestion.IngestionService;
import ru.itmo.logcollector.response.LoggingIncidentNotifier;
import ru.itmo.logcollector.response.StaticTeamResolver;
import ru.itmo.logcollector.storage.InMemoryIncidentStore;
import ru.itmo.logcollector.storage.InMemoryLabelStore;
import ru.itmo.logcollector.storage.InMemoryTelemetryStore;

/**
 * Единственное место, где выбираются реализации портов.
 * Заменили реализацию в своём модуле — поменяйте одну строку здесь.
 */
@Configuration
public class ModulesConfig {

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }

    // ---- хранилища ----
    @Bean
    TelemetryStore telemetryStore() {
        return new InMemoryTelemetryStore();
    }

    @Bean
    LabelStore labelStore() {
        return new InMemoryLabelStore();
    }

    @Bean
    IncidentStore incidentStore() {
        return new InMemoryIncidentStore();
    }

    // ---- обнаружение ----
    @Bean
    Detector ruleDetector() {
        return new RuleDetector();
    }

    @Bean
    Detector mlDetector() {
        return new MlDetector();
    }

    // ---- инциденты ----
    @Bean
    Map<DetectionApproach, IncidentCorrelator> correlators(LogCollectorProperties props) {
        Map<DetectionApproach, IncidentCorrelator> map = new EnumMap<>(DetectionApproach.class);
        for (DetectionApproach approach : DetectionApproach.values()) {
            map.put(approach, new TimeWindowCorrelator(approach, props.correlationWindow()));
        }
        return map;
    }

    @Bean
    SuspectRanker suspectRanker() {
        return new DefaultSuspectRanker();
    }

    @Bean
    IncidentExplainer incidentExplainer() {
        return new DefaultIncidentExplainer();
    }

    // ---- реакция ----
    @Bean
    TeamResolver teamResolver(LogCollectorProperties props) {
        Map<String, Team> byService = new HashMap<>();
        Map<String, Team> byId = new HashMap<>();
        for (LogCollectorProperties.TeamProperties t : props.teams()) {
            Team team = new Team(t.id(), t.name(), t.members());
            byId.put(t.id(), team);
            t.services().forEach(s -> byService.put(s, team));
        }
        return new StaticTeamResolver(byService, byId.get(props.fallbackTeam()));
    }

    @Bean
    IncidentNotifier incidentNotifier() {
        return new LoggingIncidentNotifier();
    }

    // ---- оценка ----
    @Bean
    QualityEvaluator qualityEvaluator() {
        return new LabelBasedEvaluator();
    }

    // ---- конвейер и приём ----
    @Bean
    Pipeline pipeline(TelemetryStore telemetry, List<Detector> detectors,
                      Map<DetectionApproach, IncidentCorrelator> correlators,
                      SuspectRanker ranker, IncidentExplainer explainer, TeamResolver teams,
                      IncidentStore incidents, IncidentNotifier notifier, Clock clock) {
        return new Pipeline(telemetry, detectors, correlators, ranker, explainer, teams, incidents, notifier, clock);
    }

    @Bean
    IngestionService ingestionService(Pipeline pipeline) {
        return new IngestionService(pipeline);
    }

    @Bean
    FileImporter fileImporter(IngestionService ingestion) {
        return new FileImporter(ingestion);
    }
}
