package com.local.taskboard.repository;

import com.local.taskboard.domain.Board;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BoardRepository extends MongoRepository<Board, String> {
    Optional<Board> findByName(String name);
}
