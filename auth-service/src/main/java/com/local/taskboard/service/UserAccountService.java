package com.local.taskboard.service;

import com.local.taskboard.controller.UserAccountController;
import com.local.taskboard.domain.UserAccount;
import com.local.taskboard.repository.UserAccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

/**
 * Service class for managing user account operations in the authentication
 * service.
 * This service handles user registration, lookup, and management operations.
 *
 * <p>
 * It provides methods for:
 * <ul>
 * <li>Finding users by username</li>
 * <li>Checking if a username already exists</li>
 * <li>Registering new users with encoded passwords</li>
 * </ul>
 *
 * @author TaskBoard Platform Team
 * @version 1.0
 * @since 1.0
 */
@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAccountService(UserAccountRepository userAccountRepository, PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<UserAccount> findByUsername(String username) {
        return userAccountRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return userAccountRepository.existsByUsername(username);
    }

    public UserAccount save(UserAccountController.RegisterRequest userAccount) {

        UserAccount user = UserAccount.builder()
                .username(userAccount.username())
                .password(passwordEncoder.encode(userAccount.password()))
                .roles(Set.of("ROLE_USER"))
                .build();

        return userAccountRepository.save(user);
    }
}
