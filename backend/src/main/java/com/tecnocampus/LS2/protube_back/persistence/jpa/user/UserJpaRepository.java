package com.tecnocampus.LS2.protube_back.persistence.jpa.user;

import com.tecnocampus.LS2.protube_back.domain.user.HashedPassword;
import com.tecnocampus.LS2.protube_back.domain.user.RawPassword;
import com.tecnocampus.LS2.protube_back.domain.user.User;
import com.tecnocampus.LS2.protube_back.domain.user.Username;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {
    Optional<User> findByUsername(String email);
    boolean verifyPassword(RawPassword raw, HashedPassword hashed);
}