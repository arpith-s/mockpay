package com.arpith.mockpay.transactionservice.config;

import com.arpith.mockpay.transactionservice.service.IdentityServiceClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.Duration;

@Configuration
public class IdentityServiceClientConfig {
    @Value("${application.identity-service.url}")
    String baseUrl;

    @Value("${application.identity-service.block-timeout}")
    Long blockTimeout;

    @Bean
    IdentityServiceClient identityServiceClient() {
        var client = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
        var factory = HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(client))
                .blockTimeout(Duration.ofSeconds(blockTimeout))
                .build();
        return factory.createClient(IdentityServiceClient.class);
    }
}
