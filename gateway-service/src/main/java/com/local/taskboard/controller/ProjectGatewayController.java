package com.local.taskboard.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

/**
 * REST controller for managing project-related operations through gateway
 * pattern.
 * This controller acts as a gateway/proxy to the project service, handling all
 * project-related
 * HTTP requests and forwarding them to the appropriate microservice.
 * 
 * <p>
 * It provides endpoints for:
 * <ul>
 * <li>Board management (creation, retrieval)</li>
 * <li>Card management (creation, retrieval, update, deletion)</li>
 * </ul>
 * 
 * <p>
 * All operations require authorization headers to be passed through to the
 * underlying services.
 * 
 * @author TaskBoard Platform Team
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/projects")
public class ProjectGatewayController {

        private final WebClient projectWebClient;

        public ProjectGatewayController(WebClient projectWebClient) {
                this.projectWebClient = projectWebClient;
        }

        @GetMapping("/boards")
        public Mono<ResponseEntity<String>> getBoards(
                        @RequestHeader(value = "Authorization", required = false) String authHeader) {

                return projectWebClient.get()
                                .uri("/api/projects/boards")
                                .header("Authorization", authHeader != null ? authHeader : "")
                                .retrieve()
                                .toEntity(String.class);

        }

        @PostMapping(value = "/boards", consumes = MediaType.APPLICATION_JSON_VALUE)
        public Mono<ResponseEntity<String>> createBoard(
                        @RequestHeader(value = "Authorization", required = false) String authHeader,
                        @RequestBody String body) {

                return projectWebClient.post()
                                .uri("/api/projects/boards")
                                .header("Authorization", authHeader != null ? authHeader : "")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(body)
                                .retrieve()
                                .toEntity(String.class);

        }

        @GetMapping("/boards/{id}")
        public Mono<ResponseEntity<String>> getBoardById(
                        @RequestHeader(value = "Authorization", required = false) String authHeader,
                        @PathVariable String id) {

                return projectWebClient.get()
                                .uri("/api/projects/boards/{id}", id)
                                .header("Authorization", authHeader != null ? authHeader : "")
                                .retrieve()
                                .toEntity(String.class);

        }

        @GetMapping("/cards/board/{boardId}")
        public Mono<ResponseEntity<String>> getCardsByBoard(
                        @RequestHeader(value = "Authorization", required = false) String authHeader,
                        @PathVariable String boardId,
                        @RequestParam(required = false) String column) {

                return projectWebClient.get()
                                .uri(uriBuilder -> uriBuilder
                                                .path("/api/projects/cards/board/{boardId}")
                                                .queryParamIfPresent("column", java.util.Optional.ofNullable(column))
                                                .build(boardId))
                                .header("Authorization", authHeader != null ? authHeader : "")
                                .retrieve()
                                .toEntity(String.class);

        }

        @PostMapping(value = "/cards", consumes = MediaType.APPLICATION_JSON_VALUE)
        public Mono<ResponseEntity<String>> createCard(
                        @RequestHeader(value = "Authorization", required = false) String authHeader,
                        @RequestBody String body) {

                return projectWebClient.post()
                                .uri("/api/projects/cards")
                                .header("Authorization", authHeader != null ? authHeader : "")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(body)
                                .retrieve()
                                .toEntity(String.class);
        }

        @DeleteMapping(value = "/cards/{id}")
        public Mono<ResponseEntity<String>> deleteCard(
                        @RequestHeader(value = "Authorization", required = false) String authHeader,
                        @PathVariable String id) {
                return projectWebClient.delete()
                                .uri(uriBuilder -> uriBuilder.path("/api/projects/cards/{id}").build(id))
                                .header("Authorization", authHeader != null ? authHeader : "")
                                .retrieve()
                                .toEntity(String.class);
        }

        @PutMapping(value = "/cards/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
        public Mono<ResponseEntity<String>> updateCard(
                        @RequestHeader(value = "Authorization", required = false) String authHeader,
                        @PathVariable String id, @RequestBody String body) {

                return projectWebClient.put().uri(uriBuilder -> uriBuilder.path("/api/projects/cards/{id}").build(id))
                                .header("Authorization", authHeader != null ? authHeader : "")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(body)
                                .retrieve()
                                .toEntity(String.class);
        }

        @GetMapping("/cards/{id}")
        public Mono<ResponseEntity<String>> getCardById(
                        @RequestHeader(value = "Authorization", required = false) String authHeader,
                        @PathVariable String id) {

                return projectWebClient.get().uri(uriBuilder -> uriBuilder.path("/api/projects/cards/{id}").build(id))
                                .header("Authorization", authHeader != null ? authHeader : "")
                                .retrieve()
                                .toEntity(String.class);
        }

        @GetMapping("/cards")
        public Mono<ResponseEntity<String>> getCards(
                        @RequestHeader(value = "Authorization", required = false) String authHeader) {

                return projectWebClient.get()
                                .uri("/api/projects/cards")
                                .header("Authorization", authHeader != null ? authHeader : "")
                                .retrieve()
                                .toEntity(String.class)
                                .onErrorResume(WebClientResponseException.class,
                                                ex -> Mono.just(ResponseEntity.status(ex.getStatusCode())
                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                .body(ex.getResponseBodyAsString())));
        }
}
