package com.tecnocampus.LS2.protube_back.persistence.jpa.user;

import com.tecnocampus.LS2.protube_back.domain.user.Password;
import com.tecnocampus.LS2.protube_back.domain.user.Username;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByUsername(String username);
    boolean login(Username username, Password password);
}