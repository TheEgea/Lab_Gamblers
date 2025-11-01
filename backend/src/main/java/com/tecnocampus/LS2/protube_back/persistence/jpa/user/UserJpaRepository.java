package com.tecnocampus.LS2.protube_back.persistence.jpa.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByUsernameAndPasswordHash(String username, String passwordHash);
    boolean existsByUsername(String username);
}
