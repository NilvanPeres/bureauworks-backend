package com.example.bureauworks.core.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.theokanning.openai.service.OpenAiService;


@Configuration
public class OpenAIConfig {

    @Value("${openai.api.key}")
    private String openaiApiKey;
    
    @Value("${openai.timeout:60}")
    private Integer timeout;

    @Bean
    public OpenAiService openAiService() {
        return new OpenAiService(openaiApiKey, Duration.ofSeconds(timeout));
    }
}

