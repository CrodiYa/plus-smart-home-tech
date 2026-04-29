package ru.yandex.practicum.telemetry.aggregator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.VoidDeserializer;
import org.apache.kafka.common.serialization.VoidSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.deserializer.SensorEventDeserializer;
import ru.yandex.practicum.kafka.serializer.GeneralAvroSerializer;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {

    @Value("${sht.telemetry.sensors.topic}")
    private String sensorsTopic;
    @Value("${sht.telemetry.snapshots.topic}")
    private String snapshotsTopic;

    private static final Duration CONSUME_ATTEMPT_TIMEOUT = Duration.ofMillis(1000);

    private final KafkaConsumer<String, SensorEventAvro> consumer;
    private final KafkaProducer<String, SensorsSnapshotAvro> producer;
    private final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new ConcurrentHashMap<>();

    private final SnapshotService snapshotService;

    @Autowired
    public AggregationStarter(SnapshotService snapshotService, @Value("${sht.bootstrap}") String bootstrapServer) {
        this.snapshotService = snapshotService;
        this.consumer = new KafkaConsumer<>(getConsumerConfig(bootstrapServer));
        this.producer = new KafkaProducer<>(getProducerConfig(bootstrapServer));

        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
    }

    public void start() {
        try {
            consumer.subscribe(List.of(sensorsTopic));

            while (true) {
                ConsumerRecords<String, SensorEventAvro> consumerRecords = consumer.poll(CONSUME_ATTEMPT_TIMEOUT);

                if (!consumerRecords.isEmpty()) {
                    for (ConsumerRecord<String, SensorEventAvro> record : consumerRecords) {
                        Optional<SensorsSnapshotAvro> optional = snapshotService.updateState(record.value());
                        optional.ifPresent(sensorsSnapshotAvro ->
                                this.producer.send(new ProducerRecord<>(snapshotsTopic, sensorsSnapshotAvro)));

                        currentOffsets.put(new TopicPartition(record.topic(), record.partition()),
                                new OffsetAndMetadata(record.offset() + 1));
                    }

                    producer.flush();

                    consumer.commitAsync(currentOffsets, (offsets, exception) -> {
                        if (exception != null) {
                            log.warn("Ошибка во время фиксации оффсетов: {}", offsets, exception);
                        }
                    });
                }
            }

        } catch (WakeupException ignored) {

        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {

            if (consumer != null) {
                consumer.commitSync(currentOffsets);
                consumer.close();
            }

            if (producer != null) {
                producer.flush();
                producer.close();
            }

        }
    }

    private Properties getConsumerConfig(String bootstrapServer) {
        Properties config = new Properties();
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "aggregator.id");
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, VoidDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SensorEventDeserializer.class);
        return config;
    }

    private Properties getProducerConfig(String bootstrapServer) {
        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, VoidSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GeneralAvroSerializer.class);
        return config;
    }
}
