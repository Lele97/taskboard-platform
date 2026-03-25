package com.local.taskboard.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthGatewayController {

    private final WebClient authWebClient;

    public AuthGatewayController(WebClient authWebClient) {
        this.authWebClient = authWebClient;
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<String>> register(@RequestBody String body) {
        return authWebClient.post()
                .uri("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .toEntity(String.class)
                .onErrorResume(WebClientResponseException.class, ex -> Mono.just(ResponseEntity.status(ex.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(ex.getResponseBodyAsString())));
    }

    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<String>> token(@RequestBody String body) {
        return authWebClient.post()
                .uri("/auth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .toEntity(String.class)
                .onErrorResume(WebClientResponseException.class, ex -> Mono.just(ResponseEntity.status(ex.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(ex.getResponseBodyAsString())));
    }
}
