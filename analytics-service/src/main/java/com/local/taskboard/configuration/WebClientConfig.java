package com.local.taskboard.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration class for WebClient beans.
 * This class creates and configures WebClient instances used for
 * communication with other services in the TaskBoard platform.
 *
 * <p>
 * The configuration includes:
 * <ul>
 * <li>A WebClient bean for communicating with the authentication service</li>
 * </ul>
 *
 * @author TaskBoard Platform Team
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(@Value("${services.auth.base-url}") String authBaseUrl) {
        return WebClient.builder()
                .baseUrl(authBaseUrl)
                .build();
    }
}
