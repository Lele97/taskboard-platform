package com.local.taskboard.controller;

import com.local.taskboard.domain.Board;
import com.local.taskboard.service.BoardService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing board operations in the TaskBoard platform.
 * This controller provides endpoints for creating, retrieving, and managing
 * boards
 * which represent collections of cards/tasks organized in columns.
 * 
 * <p>
 * It provides endpoints for:
 * <ul>
 * <li>Retrieving all boards</li>
 * <li>Retrieving a specific board by ID</li>
 * <li>Creating a new board</li>
 * </ul>
 * 
 * @author TaskBoard Platform Team
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/projects/boards")
public class BoardController {

    private final BoardService boardService;

    public record BoardRequest(@NotBlank String name, String description, String ownerUserId) {
    }

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping
    public List<Board> findAll() {
        return boardService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Board> findOne(@PathVariable String id) {
        return boardService.findById(id).map(resp -> ResponseEntity.ok().body(resp))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Board> save(@Valid @RequestBody BoardRequest board) {
        return new ResponseEntity<>(boardService.save(board), HttpStatus.OK);
    }
}
