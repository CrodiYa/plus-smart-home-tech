package ru.yandex.practicum.telemetry.analyzer.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class ScenarioConditionId implements Serializable {
    private Long scenarioId;
    private String sensorId;
    private Long conditionId;
}
