package ru.practicum.sht.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.sht.model.hub.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

@Mapper(componentModel = "spring")
public interface HubMapper {

    default HubEventAvro toHubEventAvro(HubEvent event) {
        HubEventAvro.Builder builder = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp());

        switch (event) {
            case DeviceAddedEvent addedEvent -> {
                DeviceAddedAvro payload = toDeviceAddedAvro(addedEvent);
                return builder.setPayload(payload).build();
            }
            case DeviceRemovedEvent removedEvent -> {
                DeviceRemovedAvro payload = toDeviceRemovedAvro(removedEvent);
                return builder.setPayload(payload).build();
            }
            case ScenarioAddedEvent scenarioAddedEvent -> {
                ScenarioAddedAvro payload = toScenarioAddedAvro(scenarioAddedEvent);
                return builder.setPayload(payload).build();
            }
            case ScenarioRemovedEvent scenarioRemovedEvent -> {
                ScenarioRemovedAvro payload = toScenarioRemovedAvro(scenarioRemovedEvent);
                return builder.setPayload(payload).build();
            }
            default -> throw new IllegalArgumentException("Unknown hub event type: " + event.getClass());
        }
    }

    @Mapping(target = "type", source = "deviceType")
    DeviceAddedAvro toDeviceAddedAvro(DeviceAddedEvent event);

    DeviceRemovedAvro toDeviceRemovedAvro(DeviceRemovedEvent event);

    ScenarioAddedAvro toScenarioAddedAvro(ScenarioAddedEvent event);

    ScenarioRemovedAvro toScenarioRemovedAvro(ScenarioRemovedEvent event);
}
