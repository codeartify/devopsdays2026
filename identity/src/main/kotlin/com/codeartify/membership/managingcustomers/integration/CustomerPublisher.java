package com.codeartify.membership.managingcustomers.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
class CustomerPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String topic;

    CustomerPublisher(KafkaTemplate<String, Object> kafkaTemplate,
                      @Value("${app.kafka.topics.managing-customer}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void publish(String key, Object event) {
        kafkaTemplate.send(topic, key, event);
    }
}
