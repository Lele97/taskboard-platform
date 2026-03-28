package com.local.taskboard.controller;

import com.local.taskboard.domain.Card;
import com.local.taskboard.service.CardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/cards")
public class CardController {

    private final CardService cardService;

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
    public ResponseEntity<Card> createCard(@RequestBody Card request) {
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
