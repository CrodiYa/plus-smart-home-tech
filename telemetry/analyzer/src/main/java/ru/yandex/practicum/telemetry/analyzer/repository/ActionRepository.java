package ru.yandex.practicum.telemetry.analyzer.repository;

import ru.yandex.practicum.telemetry.analyzer.model.Action;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionRepository extends JpaRepository<Action, Long> {
}
