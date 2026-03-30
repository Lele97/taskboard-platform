package com.local.taskboard.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.Instant;
import java.util.List;

/**
 * Entity class representing a card in the TaskBoard platform.
 * A card represents an individual task or item within a board, organized in
 * columns.
 * 
 * <p>
 * This class maps to the "cards" collection in MongoDB and contains
 * information about a card including its board association, column, title,
 * description,
 * assignee, labels, due date, and timestamps.
 * 
 * <p>
 * Note: Currently columns are represented as strings, but in the future
 * a separate Column entity may be modeled.
 * 
 * @author TaskBoard Platform Team
 * @version 1.0
 * @since 1.0
 */
@Document(collection = "cards")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {

    @Id
    private String id;

    @Indexed
    private String boardId;

    @Indexed
    private String column;

    private String title;

    private String description;

    private String assigneeUserId;

    private List<String> labels;

    private Instant dueDate;

    private Instant createdAt;

    private Instant updatedAt;
}
