package org.secured.model.request;

import jakarta.persistence.*;
import lombok.Data;

/**
 * ------------------------------------------------------------------------
 * Author   : Sanjay Krishna Narayanan
 * Created  : 9/28/26
 * Version  : 1.0
 * ------------------------------------------------------------------------
 */
@Table(name = "users")
@Entity
@Data
public class RegisterRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(
            name = "users_seq",
            sequenceName = "users_seq",
            allocationSize = 1
    )
    private Long id;

    private String username;

    @Column(name = "password_hash")
    private String passwordHash;

    private String role;
}
