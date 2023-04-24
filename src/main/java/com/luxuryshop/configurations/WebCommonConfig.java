package com.luxuryshop.configurations;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class WebCommonConfig {

    public static final Boolean USING_KAFKA = false;
    public static final String TOPIC_LOG_KAFKA = "user_log";
}
