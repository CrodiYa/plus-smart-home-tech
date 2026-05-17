package ru.practicum.telemetry.collector.grcp.handlers.hub;

import org.springframework.stereotype.Component;
import ru.practicum.telemetry.collector.kafka.KafkaEventClient;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioRemovedEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

@Component
public class ScenarioRemovedEventHandler extends BaseHubEventHandler<ScenarioRemovedEventAvro> {
    public ScenarioRemovedEventHandler(KafkaEventClient client) {
        super(client);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_REMOVED;
    }

    @Override
    protected ScenarioRemovedEventAvro mapToAvro(HubEventProto proto) {
        ScenarioRemovedEventProto event = proto.getScenarioRemoved();

        return ScenarioRemovedEventAvro.newBuilder()
                .setName(event.getName())
                .build();
    }
}
