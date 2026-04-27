package com.local.taskboard.service;

import com.local.taskboard.controller.ProjectController;
import com.local.taskboard.domain.Card;
import com.local.taskboard.repository.CardRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing card-related operations in the TaskBoard platform.
 * This service provides business logic for creating, retrieving, updating, and
 * deleting cards
 * which represent individual tasks or items within boards.
 *
 * <p>
 * The service acts as an intermediary between the CardController and
 * CardRepository,
 * handling the business logic and data transformations required for card
 * operations.
 *
 * @author TaskBoard Platform Team
 * @version 1.0
 * @since 1.0
 */
@Service
public class CardService {

    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public List<Card> findByBoardIdAndColumn(String boardId, String column) {
        return cardRepository.findByBoardIdAndColumn(boardId, column);
    }

    public List<Card> findAll() {
        return cardRepository.findAll();
    }

    public Optional<Card> findById(String id) {
        return cardRepository.findById(id);
    }

    public Card save(ProjectController.CardRequest card) {
        Card savedCard = Card.builder()
                .boardId(card.boardId())
                .column(card.column())
                .title(card.title())
                .description(card.description())
                .assigneeUserId(card.assigneeUserId())
                .labels(card.labels())
                .dueDate(card.dueDate())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        return cardRepository.save(savedCard);
    }

    public List<Card> findByBoardId(String boardId) {
        return cardRepository.findByBoardId(boardId);
    }

    public Optional<Card> updateCard(String id, Card request) {
        return cardRepository.findById(id).map(existing -> {
            if (request.getTitle() != null) {
                existing.setTitle(request.getTitle());
            }
            if (request.getDescription() != null) {
                existing.setDescription(request.getDescription());
            }
            if (request.getColumn() != null) {
                existing.setColumn(request.getColumn());
            }
            if (request.getAssigneeUserId() != null) {
                existing.setAssigneeUserId(request.getAssigneeUserId());
            }
            if (request.getLabels() != null) {
                existing.setLabels(request.getLabels());
            }
            if (request.getDueDate() != null) {
                existing.setDueDate(request.getDueDate());
            }
            existing.setUpdatedAt(Instant.now());
            return cardRepository.save(existing);
        });
    }

    public void deleteCard(String id) {
        cardRepository.deleteById(id);
    }

}
