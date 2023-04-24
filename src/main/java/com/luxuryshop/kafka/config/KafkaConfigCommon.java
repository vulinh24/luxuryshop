package com.luxuryshop.kafka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
public class KafkaConfigCommon {
    private String bootstrapServers = "localhost:9092";
    private String keySerializerClassConfig = "org.apache.kafka.common.serialization.StringSerializer";
    private String valueSerializerClassConfig = "org.apache.kafka.common.serialization.StringSerializer";
    private String acksConfig = "1";
}
