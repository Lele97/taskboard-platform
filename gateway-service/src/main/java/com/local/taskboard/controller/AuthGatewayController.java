package com.local.taskboard.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import com.local.taskboard.exception.BadCredentialsException;
import com.local.taskboard.exception.UsernameNotFoundException;
import reactor.core.publisher.Mono;

/**
 * REST controller for handling authentication-related operations through
 * gateway pattern.
 * This controller acts as a gateway/proxy to the authentication service,
 * handling all
 * authentication-related HTTP requests and forwarding them to the appropriate
 * microservice.
 * 
 * <p>
 * It provides endpoints for:
 * <ul>
 * <li>User registration</li>
 * <li>Authentication token generation</li>
 * </ul>
 * 
 * @author TaskBoard Platform Team
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/auth")
public class AuthGatewayController {

    private final WebClient authWebClient;

    public AuthGatewayController(WebClient authWebClient) {
        this.authWebClient = authWebClient;
    }

    public record RegisterRequest(
            @NotBlank(message = "Username non può essere vuoto") String username,
            @NotBlank(message = "Password non può essere vuota") String password) {
    }

    public record LoginRequest(
            @NotBlank(message = "Username non può essere vuoto") String username,
            @NotBlank(message = "Password non può essere vuota") String password) {
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<String>> register(@Valid @RequestBody RegisterRequest body) {
        return authWebClient.post()
                .uri("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .toEntity(String.class);
    }

    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<String>> token(@Valid @RequestBody LoginRequest body) {
        return authWebClient.post()
                .uri("/auth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .toEntity(String.class)
                .onErrorMap(WebClientResponseException.class, ex -> new BadCredentialsException(ex.getMessage()));
        // .onErrorMap(WebClientResponseException.class, ex -> {
        // if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED || ex.getStatusCode() ==
        // HttpStatus.BAD_REQUEST || ex.getStatusCode() == HttpStatus.FORBIDDEN) {
        // String responseBody = ex.getResponseBodyAsString().toLowerCase();
        // if (responseBody.contains("user not found") ||
        // responseBody.contains("usernamenotfound") || responseBody.contains("invalid
        // credentials") || responseBody.contains("bad credentials")) {
        // if (responseBody.contains("user not found") ||
        // responseBody.contains("usernamenotfound")) {
        // return new UsernameNotFoundException("User not found");
        // }
        // return new BadCredentialsException("Bad Credentials");
        // }
        // // try to match specific messages exactly as expected for bad login
        // return new BadCredentialsException("Bad Credentials");
        // }
        // return ex;
        // });
    }
}
