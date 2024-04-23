package com.example.orderservice.configuration;

import com.example.orderservice.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {

   @Value("${spring.kafka.bootstrap-servers}")
   private String bootstrapServers;

   @Value("${app.kafka.listenGroupId}")
   private String messageGroupId;

   @Bean
    public ProducerFactory<String, Message> messageProducerFactory(ObjectMapper objectMapper){
       Map<String, Object> config = new HashMap<>();

       config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
       config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
       config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

       return new DefaultKafkaProducerFactory<>(
               config, new StringSerializer(), new JsonSerializer<>(objectMapper));
   }

   @Bean
    public KafkaTemplate<String, Message> kafkaTemplate(ProducerFactory<String, Message> messageProducerFactory){
       return new KafkaTemplate<>(messageProducerFactory);
   }

   @Bean
    public ConsumerFactory<String, Message> messageConsumerFactory(ObjectMapper objectMapper){
       Map<String, Object> config = new HashMap<>();

       config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
       config.put(ConsumerConfig.GROUP_ID_CONFIG, messageGroupId);
       config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
       config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
       config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

       return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), new JsonDeserializer<>(objectMapper));
   }

   @Bean
   public ConcurrentKafkaListenerContainerFactory<String,Message> messageConcurrentKafkaListenerContainerFactory(
           ConsumerFactory<String,Message> orderConsumerFactory){
      ConcurrentKafkaListenerContainerFactory<String, Message> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
      containerFactory.setConsumerFactory(orderConsumerFactory);

      return containerFactory;
   }



}
