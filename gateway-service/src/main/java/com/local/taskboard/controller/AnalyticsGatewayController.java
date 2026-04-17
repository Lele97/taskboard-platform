package com.local.taskboard.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/analytics")
@AllArgsConstructor
public class AnalyticsGatewayController {

    private final WebClient analyticsWebClient;

    @GetMapping
    public Mono<ResponseEntity<String>> getAnalytics(@RequestHeader(value = "Authorization") String authHeader) {
        return analyticsWebClient.get().uri("/api/analytics")
                .header("Authorization", authHeader != null ? authHeader : "")
                .retrieve()
                .bodyToMono(String.class)
                .map(body -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(body));
    }

    @GetMapping("/{boardId}")
    public Mono<ResponseEntity<String>> getAnalyticsByBoardId(@PathVariable String boardId, @RequestHeader(value = "Authorization") String authHeader) {
        return analyticsWebClient.get().uri(uriBuilder -> uriBuilder
                        .path("/api/analytics/{boardId}")
                        .build(boardId)).header("Authorization", authHeader != null ? authHeader : "")
                .retrieve()
                .bodyToMono(String.class)
                .map(body -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(body));
    }
}
