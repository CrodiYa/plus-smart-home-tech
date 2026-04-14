package ru.practicum.sht.mappers;

import org.mapstruct.Mapper;
import ru.practicum.sht.model.sensor.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

@Mapper(componentModel = "spring")
public interface SensorMapper {

    default SensorEventAvro toSensorEventAvro(SensorEvent event) {
        SensorEventAvro.Builder builder = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp());

        switch (event) {
            case ClimateSensorEvent climateEvent -> {
                ClimateSensorAvro payload = toClimateSensorAvro(climateEvent);
                return builder.setPayload(payload).build();
            }
            case LightSensorEvent lightEvent -> {
                LightSensorAvro payload = toLightSensorAvro(lightEvent);
                return builder.setPayload(payload).build();
            }
            case MotionSensorEvent motionEvent -> {
                MotionSensorAvro payload = toMotionSensorAvro(motionEvent);
                return builder.setPayload(payload).build();
            }
            case SwitchSensorEvent switchEvent -> {
                SwitchSensorAvro payload = toSwitchSensorAvro(switchEvent);
                return builder.setPayload(payload).build();
            }
            case TemperatureSensorEvent tempEvent -> {
                TemperatureSensorAvro payload = toTemperatureSensorAvro(tempEvent);
                return builder.setPayload(payload).build();
            }
            default -> throw new IllegalArgumentException("Unknown sensor type: " + event.getClass());
        }
    }

    ClimateSensorAvro toClimateSensorAvro(ClimateSensorEvent event);

    LightSensorAvro toLightSensorAvro(LightSensorEvent event);

    MotionSensorAvro toMotionSensorAvro(MotionSensorEvent event);

    SwitchSensorAvro toSwitchSensorAvro(SwitchSensorEvent event);

    TemperatureSensorAvro toTemperatureSensorAvro(TemperatureSensorEvent event);
}
