package com.local.taskboard.repository;

import com.local.taskboard.domain.Card;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Repository interface for managing Card entities in the TaskBoard platform.
 * This interface provides CRUD operations for Card entities and custom query
 * methods
 * for card-specific operations using MongoDB as the persistence layer.
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
public interface CardRepository extends MongoRepository<Card, String> {
    List<Card> findByBoardId(String boardId);

    List<Card> findByBoardIdAndColumn(String boardId, String column);
}
