package com.local.taskboard.repository;

import com.local.taskboard.domain.Card;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CardRepository extends MongoRepository<Card, String> {
    List<Card> findByBoardId(String boardId);
    List<Card> findByBoardIdAndColumn(String boardId, String column);
}
