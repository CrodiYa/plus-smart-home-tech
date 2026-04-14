package ru.practicum.sht.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.sht.kafka.KafkaEventClient;
import ru.practicum.sht.mappers.HubMapper;
import ru.practicum.sht.mappers.SensorMapper;
import ru.practicum.sht.model.hub.HubEvent;
import ru.practicum.sht.model.sensor.SensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Service
@RequiredArgsConstructor
public class EventService {

    @Value("${sht.telemetry.sensors.topic}")
    private String sensorsTopic;
    @Value("${sht.telemetry.hubs.topic}")
    private String hubsTopic;

    private final SensorMapper sensorMapper;
    private final HubMapper hubMapper;
    private final KafkaEventClient kafkaClient;

    public void addSensorEvent(SensorEvent sensorEvent) {
        SensorEventAvro sensorEventAvro = sensorMapper.toSensorEventAvro(sensorEvent);

        kafkaClient.getProducer().send(new ProducerRecord<>(sensorsTopic, sensorEventAvro));
    }

    public void addHubEvent(HubEvent hubEvent) {
        HubEventAvro hubEventAvro = hubMapper.toHubEventAvro(hubEvent);

        kafkaClient.getProducer().send(new ProducerRecord<>(hubsTopic, hubEventAvro));
    }
}
