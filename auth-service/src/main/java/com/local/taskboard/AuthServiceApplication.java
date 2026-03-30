package com.local.taskboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Authentication Service application.
 * This class bootstraps the Spring Boot application for the authentication
 * service
 * which handles user registration, authentication, and JWT token generation.
 *
 * <p>
 * The application uses Spring Boot's auto-configuration features along with
 * custom security configurations defined in the configuration package.
 *
 * @author TaskBoard Platform Team
 * @version 1.0
 * @since 1.0
 */
@SpringBootApplication
public class AuthServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
}