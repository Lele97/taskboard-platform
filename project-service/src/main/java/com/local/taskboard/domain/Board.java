package com.local.taskboard.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

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

    // per ora semplice, poi lo legheremo a organizzazione/team
    private String ownerUserId;

    private Instant createdAt;
    private Instant updatedAt;
}
