package com.tecnocampus.LS2.protube_back.domain.auth;

import com.tecnocampus.LS2.protube_back.domain.user.*;

import java.util.Optional;

public interface UserAuthPort {
    Optional<User> loadByUsername(Username username);
    boolean verifyPassword(RawPassword raw, HashedPassword hashed);
}