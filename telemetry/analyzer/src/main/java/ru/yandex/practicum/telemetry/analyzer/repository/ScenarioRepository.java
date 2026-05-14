package ru.yandex.practicum.telemetry.analyzer.repository;

import ru.yandex.practicum.telemetry.analyzer.model.Scenario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScenarioRepository extends JpaRepository<Scenario, Long> {
    List<Scenario> findByHubId(String hubId);

    Optional<Scenario> findByHubIdAndName(String hubId, String name);

    @EntityGraph(attributePaths = {
            "conditions.sensor",
            "conditions.condition",
            "actions.sensor",
            "actions.action"
    })
    List<Scenario> findWithSensorsByHubId(String hubId);
}
