package com.local.taskboard.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

/**
 * Entity class representing a user account in the authentication service.
 * This class maps to the "user_account" collection in MongoDB and represents
 * a registered user with their credentials and roles.
 *
 * <p>
 * The class uses Lombok annotations to automatically generate getters, setters,
 * constructors, and builder methods. It includes:
 * <ul>
 * <li>A unique MongoDB ObjectId as the primary key</li>
 * <li>An indexed username field for fast lookups</li>
 * <li>A password field (stored encrypted)</li>
 * <li>A set of roles for authorization purposes</li>
 * </ul>
 *
 * @author TaskBoard Platform Team
 * @version 1.0
 * @since 1.0
 */
@Document(collection = "user_account")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccount {

    @Id
    private String id;

    @Indexed
    private String username;

    private String password;

    private Set<String> roles;
}
