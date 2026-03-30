package com.local.taskboard.repository;

import com.local.taskboard.domain.Board;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Repository interface for managing Board entities in the TaskBoard platform.
 * This interface provides CRUD operations for Board entities and custom query
 * methods
 * for board-specific operations using MongoDB as the persistence layer.
 * 
 * <p>
 * It extends Spring Data MongoDB's MongoRepository which provides generic CRUD
 * operations
 * and automatically implements common database operations based on method
 * naming conventions.
 * 
 * @author TaskBoard Platform Team
 * @version 1.0
 * @since 1.0
 */
public interface BoardRepository extends MongoRepository<Board, String> {
    Optional<Board> findByName(String name);
}
