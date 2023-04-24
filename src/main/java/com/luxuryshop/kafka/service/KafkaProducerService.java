package com.luxuryshop.kafka.service;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, String> producer;

    public void sendMessage(String topic, String key, String value) {
        Thread thread = new Thread(() -> {
            ProducerRecord<String, String> rec = new ProducerRecord<>(topic, key, value);
            producer.send(rec);
            producer.flush();
        });
        thread.start();
    }
}
