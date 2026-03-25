package com.local.taskboard.service;

import com.local.taskboard.domain.Board;
import com.local.taskboard.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

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

    public Board save(Board board) {
        Board savedBoard = Board.builder()
                .name(board.getName())
                .description(board.getDescription())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .ownerUserId(board.getOwnerUserId()).build();

        return boardRepository.save(savedBoard);
    }
}
