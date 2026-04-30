package ru.yandex.practicum.telemetry.aggregator;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SnapshotService {

    private final Map<String, SensorsSnapshotAvro> snapshots = new ConcurrentHashMap<>();

    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {

        final SensorsSnapshotAvro snapshot = snapshots.computeIfAbsent(
                event.getHubId(),
                k -> SensorsSnapshotAvro.newBuilder()
                        .setHubId(event.getHubId())
                        .setTimestamp(event.getTimestamp())
                        .setSensorsState(new ConcurrentHashMap<>())
                        .build()
        );

        SensorStateAvro oldState = snapshot.getSensorsState().get(event.getId());

        if (oldState != null && (Objects.equals(oldState.getData(), event.getPayload()) ||
             oldState.getTimestamp().isAfter(event.getTimestamp()))) {
            return Optional.empty();
        }

        SensorStateAvro stateAvro = SensorStateAvro.newBuilder()
                .setData(event.getPayload())
                .setTimestamp(event.getTimestamp())
                .build();

        snapshot.getSensorsState().put(event.getId(), stateAvro);
        snapshot.setTimestamp(event.getTimestamp());

        return Optional.of(snapshot);
    }
}
