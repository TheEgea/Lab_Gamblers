package com.tecnocampus.LS2.protube_back.domain.user;

import java.util.Objects;
import java.util.Set;

public final class User {
    private final UserId id;
    private final Username username;
    private final Password password;
    private final Role roles;

    public User(UserId id, Username username, Password password, Role roles) {
        this.id = Objects.requireNonNull(id);
        this.username = Objects.requireNonNull(username);
        this.password = Objects.requireNonNull(password);
        this.roles = Objects.requireNonNull(roles);
    }

    public UserId id() { return id; }
    public Username username() { return username; }
    public Password password() { return password; }
    public Role roles() { return roles; }
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username=" + username +
                ", password=" + password +
                ", roles=" + roles +
                '}';
    }

}