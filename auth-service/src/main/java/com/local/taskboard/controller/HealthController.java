package com.local.taskboard.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for health check endpoint of the authentication service.
 * This controller provides a simple endpoint to verify that the auth service is
 * running.
 *
 * @author TaskBoard Platform Team
 * @version 1.0
 * @since 1.0
 */
@RestController
public class HealthController {
    @GetMapping("/api/auth/health")
    public String health() {
        return "auth-service ok";
    }
}
