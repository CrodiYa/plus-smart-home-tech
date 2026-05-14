package ru.practicum.sht.grcp.handlers.sensor;

import org.springframework.stereotype.Component;
import ru.practicum.sht.kafka.KafkaEventClient;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Component
public class SwitchSensorEventHandler extends BaseSensorEventHandler<SwitchSensorAvro> {
    public SwitchSensorEventHandler(KafkaEventClient client) {
        super(client);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR;
    }

    @Override
    protected SwitchSensorAvro mapToAvro(SensorEventProto proto) {
        SwitchSensorProto event = proto.getSwitchSensor();

        return SwitchSensorAvro.newBuilder()
                .setState(event.getState())
                .build();
    }
}
