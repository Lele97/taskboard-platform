package com.local.taskboard.configuration;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Set;

@Component
public class AuthUserClient {

    private final WebClient authWebClient;
    public record UserInfo(String username, Set<String> roles) {}

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
