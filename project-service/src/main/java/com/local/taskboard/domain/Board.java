package com.local.taskboard.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * Entity class representing a board in the TaskBoard platform.
 * A board represents a collection of cards/tasks organized in columns.
 * 
 * <p>
 * This class maps to the "board" collection in MongoDB and contains
 * information about a board including its name, description, owner, and
 * timestamps.
 * 
 * @author TaskBoard Platform Team
 * @version 1.0
 * @since 1.0
 */
@Document(collection = "board")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board {

    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    private String description;

    private String ownerUserId;

    private Instant createdAt;

    private Instant updatedAt;
}
