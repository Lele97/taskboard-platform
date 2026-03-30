package com.local.taskboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Gateway Service application.
 * This class bootstraps the Spring Boot application for the gateway service
 * which acts as an API gateway routing requests to appropriate microservices.
 *
 * <p>
 * The application uses Spring Cloud Gateway for routing and load balancing
 * between the various microservices in the TaskBoard platform. It also handles
 * cross-cutting concerns like CORS configuration and global exception handling.
 *
 * @author TaskBoard Platform Team
 * @version 1.0
 * @since 1.0
 */
@SpringBootApplication
public class GatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }
}
