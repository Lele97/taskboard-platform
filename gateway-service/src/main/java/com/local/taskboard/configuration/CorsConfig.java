package com.local.taskboard.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Configuration class for Cross-Origin Resource Sharing (CORS) settings.
 * This class configures CORS policies for the gateway service to allow
 * cross-origin requests from the frontend application.
 *
 * <p>
 * The configuration permits requests from http://localhost:4200 (Angular
 * frontend)
 * with all HTTP methods (GET, POST, PUT, DELETE, OPTIONS) and headers.
 * Credentials are allowed, and the preflight response is cached for 1 hour.
 *
 * @author TaskBoard Platform Team
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        corsConfig.setMaxAge(3600L);
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfig.setAllowedHeaders(Arrays.asList("*"));
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}
