package ru.yandex.practicum.telemetry.analyzer.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.VoidDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.deserializer.HubEventDeserializer;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.analyzer.service.AnalyzerService;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Component
public class HubEventProcessor implements Runnable {

    @Value("${sht.telemetry.hubs.topic}")
    private String hubsTopic;
    private final Duration consumeAttemptTimeout;

    private final AnalyzerService analyzerService;
    private final KafkaConsumer<String, HubEventAvro> consumer;
    private final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new ConcurrentHashMap<>();

    @Autowired
    public HubEventProcessor(AnalyzerService analyzerService,
                             @Value("${sht.bootstrap}") String bootstrapServer,
                             @Value("${sht.consumeAttemptTimeout}") int consumeAttemptTimeout) {
        this.analyzerService = analyzerService;
        this.consumer = new KafkaConsumer<>(getConsumerConfig(bootstrapServer));
        this.consumeAttemptTimeout = Duration.ofMillis(consumeAttemptTimeout);
        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
    }

    @Override
    public void run() {
        try {
            consumer.subscribe(List.of(hubsTopic));

            while (true) {
                ConsumerRecords<String, HubEventAvro> records = consumer.poll(consumeAttemptTimeout);
                if (records.isEmpty()) {
                    continue;
                }

                for (ConsumerRecord<String, HubEventAvro> record : records) {
                    try {
                        analyzerService.handleHubEvent(record.value());
                        currentOffsets.put(new TopicPartition(record.topic(), record.partition()),
                                new OffsetAndMetadata(record.offset() + 1));
                    } catch (Exception e) {
                        log.error("Ошибка при обработке события хаба с оффсетом {}", record.offset(), e);
                    }
                }

                consumer.commitAsync(currentOffsets, (offsets, exception) -> {
                    if (exception != null) {
                        log.warn("Ошибка при фиксации оффсетов событий хаба: {}", offsets, exception);
                    }
                });
            }
        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Ошибка при обработке событий хаба", e);
        } finally {
            try {
                consumer.commitSync(currentOffsets);
            } finally {
                consumer.close();
            }
        }
    }

    private Properties getConsumerConfig(String bootstrapServer) {
        Properties config = new Properties();
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "analyzer.hub.id");
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, VoidDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, HubEventDeserializer.class);
        return config;
    }
}
