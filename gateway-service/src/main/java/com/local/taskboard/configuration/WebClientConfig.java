package com.local.taskboard.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient authWebClient(
            @Value("${services.auth.base-url}") String authBaseUrl) {
        return WebClient.builder()
                .baseUrl(authBaseUrl)
                .build();
    }

    @Bean
    public WebClient projectWebClient(
            @Value("${services.project.base-url}") String projectBaseUrl) {
        return WebClient.builder()
                .baseUrl(projectBaseUrl)
                .build();
    }
}
