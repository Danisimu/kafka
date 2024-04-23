package com.example.orderservice.listener;

import com.example.orderservice.model.Message;
import com.example.orderservice.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderListener {

    private final KafkaTemplate<String, Message> kafkaTemplate;

    @Value("${app.kafka.sendTopic}")
    private String sendTopic;

    @KafkaListener(topics = "${app.kafka.listenTopic}",
            groupId = "${app.kafka.listenGroupId}",
            containerFactory = "messageConcurrentKafkaListenerContainerFactory")
    public void listen(@Payload Order order,
                       @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) UUID key,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                       @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                       @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp
                       ){

        log.info("Received message: {}", order);
        log.info("Key: {}; Partition: {}; Topic: {}, Timestamp: {}", key, partition, topic, timestamp);

        kafkaTemplate.send(sendTopic, new Message("CREATED", Instant.now()));

    }

}
