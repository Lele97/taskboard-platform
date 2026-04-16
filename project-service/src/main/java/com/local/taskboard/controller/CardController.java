package com.local.taskboard.controller;

import com.local.taskboard.domain.Card;
import com.local.taskboard.service.CardService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

/**
 * REST controller for managing card operations in the TaskBoard platform.
 * This controller provides endpoints for creating, retrieving, updating, and
 * deleting cards
 * which represent individual tasks or items within boards.
 * 
 * <p>
 * It provides endpoints for:
 * <ul>
 * <li>Retrieving all cards</li>
 * <li>Retrieving cards by board ID and optional column</li>
 * <li>Retrieving a specific card by ID</li>
 * <li>Creating a new card</li>
 * <li>Updating an existing card</li>
 * <li>Deleting a card</li>
 * </ul>
 * 
 * @author TaskBoard Platform Team
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/projects/cards")
public class CardController {

    private final CardService cardService;

    public record CardRequest(@NotBlank String boardId, @NotBlank String column, @NotBlank String title,
            String description, String assigneeUserId, List<String> labels, Instant dueDate) {
    }

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping
    public ResponseEntity<List<Card>> findAll() {
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

    @PostMapping
    public ResponseEntity<Card> createCard(@Valid @RequestBody CardRequest request) {
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
