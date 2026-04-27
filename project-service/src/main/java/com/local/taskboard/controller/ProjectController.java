package com.local.taskboard.controller;

import com.local.taskboard.domain.Board;
import com.local.taskboard.domain.Card;
import com.local.taskboard.service.BoardService;
import com.local.taskboard.service.CardService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    private final BoardService boardService;
    private final CardService cardService;

    public ProjectController(BoardService boardService, CardService cardService) {
        this.boardService = boardService;
        this.cardService = cardService;
    }

    public record BoardRequest(@NotBlank String name, String description, String ownerUserId) {
    }

    public record CardRequest(@NotBlank String boardId, @NotBlank String column, @NotBlank String title,
                              String description, String assigneeUserId, List<String> labels, Instant dueDate) {
    }

    @GetMapping("/boards")
    public List<Board> findAllBoars() {
        return boardService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Board> findOne(@PathVariable String id) {
        return boardService.findById(id).map(resp -> ResponseEntity.ok().body(resp))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("boards")
    public ResponseEntity<Board> save(@Valid @RequestBody ProjectController.BoardRequest board) {
        return new ResponseEntity<>(boardService.save(board), HttpStatus.OK);
    }

    @GetMapping("/cards")
    public ResponseEntity<List<Card>> findAllCards() {
        return ResponseEntity.ok(cardService.findAll());
    }

    @GetMapping("/board/{boardId}")
    public List<Card> getCardsByBoard(@PathVariable String boardId,
                                      @RequestParam(required = false) String column) {
        if (column != null) {
            return cardService.findByBoardIdAndColumn(boardId, column);
        }
        return cardService.findByBoardId(boardId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> getCardById(@PathVariable String id) {
        return cardService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/boards")
    public ResponseEntity<Card> createCard(@Valid @RequestBody ProjectController.CardRequest request) {
        Card saved = cardService.save(request);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Card> updateCard(@PathVariable String id,
                                           @RequestBody Card request) {
        return cardService.updateCard(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable String id) {
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }
}