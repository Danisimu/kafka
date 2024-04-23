package com.example.orderservice.web.controller;

import com.example.orderservice.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    @Value("${app.kafka.sendTopic}")
    private String topic;

    private final KafkaTemplate<String, Order> kafkaTemplate;

    @PostMapping
    public ResponseEntity<String> send(@RequestBody Order order){

        kafkaTemplate.send(topic, order);

        return ResponseEntity.ok("Message sent to kafka");
    }
}
