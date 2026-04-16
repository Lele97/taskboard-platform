package com.local.taskboard.configuration;

import com.local.taskboard.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Authentication Filter for validating and processing JWT tokens.
 * This filter intercepts HTTP requests to extract and validate JWT tokens
 * from the Authorization header, and sets up Spring Security authentication
 * based on the token validity and user information retrieved from the
 * authentication service.
 *
 * <p>
 * The filter performs the following steps:
 * <ul>
 * <li>Extracts the JWT token from the Authorization header</li>
 * <li>Validates the token using JwtService</li>
 * <li>Retrieves user information from the authentication service</li>
 * <li>Sets up Spring Security authentication context if token is valid</li>
 * </ul>
 *
 * @author TaskBoard Platform Team
 * @version 1.0
 * @since 1.0
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AuthUserClient authUserClient;

    public JwtAuthenticationFilter(JwtService jwtService, AuthUserClient authUserClient) {
        this.jwtService = jwtService;
        this.authUserClient = authUserClient;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7).trim();

        if (jwt.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            username = jwtService.extractUsername(jwt);
        } catch (Exception e) {
            // Token malformato o non valido – lo si tratta come non autenticato
            filterChain.doFilter(request, response);
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Nota: chiamiamo l'Auth Service passandogli l'header completo "Bearer <token>"
            var userInfo = authUserClient.getCurrentUser(authHeader);

            if (userInfo != null && jwtService.isTokenValid(jwt, userInfo.username())) {
                var authorities = userInfo.roles().stream()
                        .map(org.springframework.security.core.authority.SimpleGrantedAuthority::new)
                        .toList();

                var authToken = new UsernamePasswordAuthenticationToken(
                        userInfo.username(), null, authorities);
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);

    }
}
