package com.example.bureauworks.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.cdimascio.dotenv.Dotenv;


@Configuration
public class EnvConfig {
    
    @Bean
    public Dotenv dotenv() {
        Dotenv dotev = Dotenv.load();

        dotev.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

        return dotev;
    }
}

