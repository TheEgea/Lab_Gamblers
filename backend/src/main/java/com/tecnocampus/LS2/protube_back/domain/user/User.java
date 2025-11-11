package com.tecnocampus.LS2.protube_back.domain.user;

import lombok.Setter;

import java.util.Objects;
import java.util.Set;

public final class User {
    private final UserId id;
    private final Username username;
    private Password password;
    @Setter
    private Role roles;
    private String email;

    public User(UserId id, Username username, Password password, Role roles, String email) {
        this.id = Objects.requireNonNull(id);
        this.username = Objects.requireNonNull(username);
        this.password = Objects.requireNonNull(password);
        this.roles = Objects.requireNonNull(roles);
        this.email = Objects.requireNonNull(email);
    }

    public UserId id() {
        return id;
    }

    public Username username() {
        return username;
    }

    public Password password() {
        return password;
    }

    public Role roles() {
        return roles;
    }
    public String email() {
        return email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username=" + username +
                ", password=" + password +
                ", roles=" + roles +
                '}';
    }

    public void setPassword(String hashedPassword) {
        this.password = new Password(hashedPassword);
    }
}