package ru.practicum.sht.grcp.handlers.hub;

import org.springframework.stereotype.Component;
import ru.practicum.sht.kafka.KafkaEventClient;
import ru.yandex.practicum.grpc.telemetry.event.DeviceRemovedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;

@Component
public class DeviceRemovedEventHandler extends BaseHubEventHandler<DeviceRemovedEventAvro> {
    public DeviceRemovedEventHandler(KafkaEventClient client) {
        super(client);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_REMOVED;
    }

    @Override
    protected DeviceRemovedEventAvro mapToAvro(HubEventProto proto) {
        DeviceRemovedEventProto event = proto.getDeviceRemoved();

        return DeviceRemovedEventAvro.newBuilder()
                .setId(event.getId())
                .build();
    }
}
