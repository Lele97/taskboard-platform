package com.local.taskboard.service;

import com.local.taskboard.controller.ProjectController;
import com.local.taskboard.domain.Board;
import com.local.taskboard.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing board-related operations in the TaskBoard
 * platform.
 * This service provides business logic for creating, retrieving, and managing
 * boards
 * which represent collections of cards/tasks organized in columns.
 * 
 * <p>
 * The service acts as an intermediary between the BoardController and
 * BoardRepository,
 * handling the business logic and data transformations required for board
 * operations.
 * 
 * @author TaskBoard Platform Team
 * @version 1.0
 * @since 1.0
 */
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Optional<Board> findByName(String name) {
        return boardRepository.findByName(name);
    }

    public Optional<Board> findById(String id) {
        return boardRepository.findById(id);
    }

    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    public Board save(ProjectController.BoardRequest board) {
        Board savedBoard = Board.builder()
                .name(board.name())
                .description(board.description())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .ownerUserId(board.ownerUserId()).build();

        return boardRepository.save(savedBoard);
    }
}
