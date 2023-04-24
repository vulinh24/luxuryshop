package com.luxuryshop.kafka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luxuryshop.configurations.WebCommonConfig;
import com.luxuryshop.entities.ShopLog;
import com.luxuryshop.kafka.UserLogKafka;
import com.luxuryshop.repositories.ShopLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class KafkaConsumerService {

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    ShopLogRepository shopLogRepository;
    @KafkaListener(topics = WebCommonConfig.TOPIC_LOG_KAFKA,
            groupId = "consumer_log")
    public void consume(List<String> message) {
        final List<ShopLog> userLogs = message.stream()
                .map(s -> {
                    try {
                        return objectMapper.readValue(s, UserLogKafka.class);
                    } catch (JsonProcessingException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .map(userLogKafka -> {
                    ShopLog shopLog = new ShopLog();
                    shopLog.setAction(userLogKafka.getAction());
                    shopLog.setOwnerId(userLogKafka.getOwnerId());
                    shopLog.setProductId(userLogKafka.getProductId());
                    shopLog.setPoint(UserLogKafka.Action.getPointFromLabel(userLogKafka.getAction()));
                    return shopLog;
                })
                .collect(Collectors.toList());
        // handle
        shopLogRepository.saveAll(userLogs);
    }
}
