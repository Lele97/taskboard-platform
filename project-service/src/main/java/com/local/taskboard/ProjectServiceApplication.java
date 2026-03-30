package com.local.taskboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Project Service application.
 * This class bootstraps the Spring Boot application for the project service
 * which manages boards and cards for the TaskBoard platform.
 *
 * <p>
 * The application handles core project management functionality including:
 * <ul>
 * <li>Board creation and management</li>
 * <li>Card creation, retrieval, update, and deletion</li>
 * <li>Integration with authentication service for user validation</li>
 * </ul>
 *
 * @author TaskBoard Platform Team
 * @version 1.0
 * @since 1.0
 */
@SpringBootApplication
public class ProjectServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjectServiceApplication.class, args);
    }
}
