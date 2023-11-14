package com.arpith.mockpay.identityservice.config;

import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public Gson getGson() {
        return new Gson();
    }
}
