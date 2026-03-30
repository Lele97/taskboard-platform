package com.local.taskboard.repository;

import com.local.taskboard.domain.UserAccount;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Repository interface for managing UserAccount entities in MongoDB.
 * This interface extends Spring Data MongoDB's MongoRepository to provide
 * CRUD operations for UserAccount entities with custom query methods.
 *
 * <p>
 * It provides methods for:
 * <ul>
 * <li>Checking if a username already exists</li>
 * <li>Finding a user by username</li>
 * </ul>
 *
 * @author TaskBoard Platform Team
 * @version 1.0
 * @since 1.0
 */
public interface UserAccountRepository extends MongoRepository<UserAccount, String> {

    boolean existsByUsername(String username);

    Optional<UserAccount> findByUsername(String username);
}
