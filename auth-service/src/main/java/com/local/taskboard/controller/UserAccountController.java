package com.local.taskboard.controller;

import com.local.taskboard.service.JwtService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import com.local.taskboard.service.UserAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/auth")
public class UserAccountController {

    private final UserAccountService userAccountService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;// la config la aggiungiamo dopo

    public record RegisterRequest(@NotBlank String username,
            @NotBlank String password) {
    }

    public record LoginRequest(@NotBlank String username,
            @NotBlank String password) {
    }

    public record UserInfoResponse(String username, Set<String> roles) {}

    public record TokenResponse(String token) {
    }

    public UserAccountController(UserAccountService userAccountService, AuthenticationManager authenticationManager,
            JwtService jwtService) {
        this.userAccountService = userAccountService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @GetMapping("/user_authenticated")
    public ResponseEntity<UserInfoResponse> userInfoAuthenticated(Authentication authentication) {
        var principal = (UserDetails) authentication.getPrincipal();
        var roles = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(java.util.stream.Collectors.toSet());

        return ResponseEntity.ok(new UserInfoResponse(principal.getUsername(), roles));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        if (userAccountService.existsByUsername(req.username())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        userAccountService.save(req);
        return ResponseEntity.ok("Registered");
    }

    @PostMapping("/token")
    public ResponseEntity<?> token(@Valid @RequestBody LoginRequest req) {
        var authToken = new UsernamePasswordAuthenticationToken(
                req.username(), req.password());
        var auth = authenticationManager.authenticate(authToken);

        String jwt = jwtService.generateToken((UserDetails) auth.getPrincipal());
        return ResponseEntity.ok(new TokenResponse(jwt));
    }
}
