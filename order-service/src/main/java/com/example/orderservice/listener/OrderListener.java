package com.example.orderservice.listener;

import com.example.orderservice.model.Message;
import com.example.orderservice.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class OrderListener {



    @KafkaListener(topics = "${app.kafka.listenTopic}",
            groupId = "${app.kafka.listenGroupId}",
            containerFactory = "orderConcurrentKafkaListenerContainerFactory")
    public void listen(@Payload Message message,
                       @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) UUID key,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                       @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                       @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp,
                       @Header(KafkaHeaders.GROUP_ID) String groupId
                       ){

        log.info("Received message: {}", message);
        log.info("Key: {}; Partition: {}; Topic: {}, Timestamp: {}, GroupId: {}", key, partition, topic, timestamp, groupId);

    }

}
