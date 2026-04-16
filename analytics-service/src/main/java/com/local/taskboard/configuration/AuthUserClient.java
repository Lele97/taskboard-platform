package com.local.taskboard.configuration;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Set;

/**
 * Client component for interacting with the authentication service.
 * This class provides methods to communicate with the authentication service
 * to retrieve user information based on JWT tokens.
 *
 * <p>
 * The client uses WebClient to make HTTP calls to the authentication service
 * endpoint /auth/user_authenticated to validate tokens and retrieve user
 * details.
 *
 * @author TaskBoard Platform Team
 * @version 1.0
 * @since 1.0
 */
@Component
public class AuthUserClient {

    private final WebClient authWebClient;

    public record UserInfo(String username, Set<String> roles) {
    }

    public AuthUserClient(WebClient authWebClient) {
        this.authWebClient = authWebClient;
    }

    public UserInfo getCurrentUser(String bearerToken) {
        return authWebClient.get()
                .uri("/auth/user_authenticated")
                .header("Authorization", bearerToken)
                .retrieve()
                .bodyToMono(UserInfo.class)
                .block(); // per semplicità: siamo in filtro servlet sincrono
    }
}
