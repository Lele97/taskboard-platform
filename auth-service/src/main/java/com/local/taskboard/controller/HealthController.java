package com.local.taskboard.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/api/auth/health")
    public String health() {
        return "auth-service ok";
    }
}
