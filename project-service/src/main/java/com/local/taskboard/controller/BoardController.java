package com.local.taskboard.controller;

import com.local.taskboard.domain.Board;
import com.local.taskboard.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/boards")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping
    public List<Board> findAll() {
        return boardService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Board> findOne(@PathVariable String id) {
        return boardService.findById(id).map(resp -> ResponseEntity.ok().body(resp)).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Board> save(@RequestBody Board board) {
        return new ResponseEntity<>(boardService.save(board), HttpStatus.OK);
    }
}
