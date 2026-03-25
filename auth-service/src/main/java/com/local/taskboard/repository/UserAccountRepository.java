package com.local.taskboard.repository;

import com.local.taskboard.domain.UserAccount;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserAccountRepository extends MongoRepository<UserAccount,String> {

    boolean existsByUsername(String username);
    Optional<UserAccount> findByUsername(String username);
}
