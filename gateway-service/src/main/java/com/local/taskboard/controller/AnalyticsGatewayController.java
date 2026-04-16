package com.local.taskboard.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/analytics")
@AllArgsConstructor
public class AnalyticsGatewayController {

    private final WebClient analyticsWebClient;

    @GetMapping
    public Mono<ResponseEntity<Flux<String>>> getAnalytics(@RequestHeader(value = "Authorization", required = true) String authHeader) {
        return analyticsWebClient.get().uri("/api/analytics")
                .header("Authorization", authHeader != null ? authHeader : "")
                .retrieve()
                .toEntityFlux(String.class);
    }

    @GetMapping("/{boardId}")
    public Mono<ResponseEntity<String>> getAnalyticsByBoardId(@PathVariable String boardId, @RequestHeader(value = "Authorization", required = true) String authHeader) {
        return analyticsWebClient.get().uri(uriBuilder -> uriBuilder
                        .path("/api/analytics/{boardId}")
                        .build(boardId)).header("Authorization", authHeader != null ? authHeader : "")
                .retrieve()
                .toEntity(String.class);
    }
}
