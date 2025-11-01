package com.tecnocampus.LS2.protube_back.persistence.jpa.user;

import com.tecnocampus.LS2.protube_back.domain.user.Role;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String role = "USER";  // String puro

    protected UserEntity() {}

    public UserEntity(UUID id, String username, String passwordHash, String role) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role != null ? role : "USER";
    }



    public UUID getId() { return id; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public Role getRole() { return Role.valueOf(role); }

    public void setId(UUID id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setRole(String role) { this.role = role; }
}