package com.local.taskboard.service;

import com.local.taskboard.controller.UserAccountController;
import com.local.taskboard.domain.UserAccount;
import com.local.taskboard.repository.UserAccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

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
