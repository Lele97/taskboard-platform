package com.local.taskboard;

import com.local.taskboard.controller.UserAccountController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        mongoTemplate.getDb().drop();
    }

    @Test
    void registerAndLoginTest() {
        // Register
        UserAccountController.RegisterRequest regReq = new UserAccountController.RegisterRequest("newuser", "newpass");
        ResponseEntity<String> regRes = restTemplate.postForEntity("/auth/register", regReq, String.class);
        assertThat(regRes.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(regRes.getBody()).isEqualTo("Registered");

        // Login
        UserAccountController.LoginRequest logReq = new UserAccountController.LoginRequest("newuser", "newpass");
        ResponseEntity<UserAccountController.TokenResponse> logRes = restTemplate.postForEntity("/auth/token", logReq, UserAccountController.TokenResponse.class);
        assertThat(logRes.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(logRes.getBody()).isNotNull();
        assertThat(logRes.getBody().token()).isNotBlank();
    }
}
