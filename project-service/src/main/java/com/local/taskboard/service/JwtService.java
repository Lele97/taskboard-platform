package com.local.taskboard.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.function.Function;

/**
 * Service class for handling JWT (JSON Web Token) operations.
 * This service provides methods for extracting information from JWT tokens,
 * validating tokens, and managing the signing key for token verification.
 *
 * <p>
 * The service uses the io.jsonwebtoken library to parse and validate JWT tokens
 * and retrieves the signing key from application properties.
 *
 * @author TaskBoard Platform Team
 * @version 1.0
 * @since 1.0
 */
@Service
public class JwtService {

    @Value("${security.jwt.secret}")
    private String secretKey;

    /**
     * Extracts the username (subject) from a JWT token.
     *
     * @param token the JWT token to extract the username from
     * @return the username contained in the token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from a JWT token using a resolver function.
     *
     * @param token    the JWT token to extract the claim from
     * @param resolver a function that determines which claim to extract
     * @param <T>      the type of the claim to extract
     * @return the extracted claim value
     */
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        var claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return resolver.apply(claims);
    }

    /**
     * Validates a JWT token by checking if the username in the token matches
     * the target username.
     *
     * @param token          the JWT token to validate
     * @param targetUsername the username to compare against the token's username
     * @return true if the token is valid and the usernames match, false otherwise
     */
    public boolean isTokenValid(String token, String targetUsername) {
        String username = extractUsername(token);
        // Se volessimo, potremmo anche controllare expiration qui
        return username.equals(targetUsername);
    }

    /**
     * Gets the signing key used for JWT token verification.
     * The key is decoded from the base64-encoded secret key stored in application
     * properties.
     *
     * @return the SecretKey used for signing and verifying JWT tokens
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
