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

    // in seguito potremo modellare una entity separata "Column"
    @Indexed
    private String column; // es. "TODO", "IN_PROGRESS", "DONE"

    private String title;
    private String description;

    private String assigneeUserId;

    private List<String> labels;

    private Instant dueDate;

    private Instant createdAt;
    private Instant updatedAt;
}
