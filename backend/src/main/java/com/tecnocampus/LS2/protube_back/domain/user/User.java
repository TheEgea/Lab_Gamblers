package com.tecnocampus.LS2.protube_back.domain.user;

import java.util.Objects;
import java.util.Set;

public final class User {
    private final UserId id;
    private final Username username;
    private final Password password;
    private final Set<Role> roles;

    public User(UserId id, Username username, Password password, Set<Role> roles) {
        this.id = Objects.requireNonNull(id);
        this.username = Objects.requireNonNull(username);
        this.password = Objects.requireNonNull(password);
        this.roles = Objects.requireNonNull(roles);
    }

    public UserId id() { return id; }
    public Username username() { return username; }
    public Password password() { return password; }
    public Set<Role> roles() { return roles; }

    public boolean hasRole(Role role) { return roles.contains(role); }
}