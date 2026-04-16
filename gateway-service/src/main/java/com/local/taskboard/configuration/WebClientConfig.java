package com.local.taskboard.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration class for WebClient beans used by the gateway service.
 * This class creates WebClient instances for communicating with downstream
 * microservices (authentication service and project service).
 *
 * <p>
 * The configuration creates two separate WebClient beans:
 * <ul>
 * <li>authWebClient: For communication with the authentication service</li>
 * <li>projectWebClient: For communication with the project service</li>
 * </ul>
 *
 * <p>
 * Base URLs for these services are configured through application properties:
 * <ul>
 * <li>services.auth.base-url: Base URL for authentication service</li>
 * <li>services.project.base-url: Base URL for project service</li>
 * </ul>
 *
 * @author TaskBoard Platform Team
 * @version 1.0
 * @since 1.0
 */
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

    @Bean
    public WebClient analyticsWebClient(@Value("${services.analytics.base-url}") String analyticsBaseUrl) {
        return WebClient.builder()
                .baseUrl(analyticsBaseUrl)
                .build();
    }
}
