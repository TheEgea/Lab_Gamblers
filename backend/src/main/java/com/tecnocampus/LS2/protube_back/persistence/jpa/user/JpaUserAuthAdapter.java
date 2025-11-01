package com.tecnocampus.LS2.protube_back.persistence.jpa.user;

import com.tecnocampus.LS2.protube_back.application.dto.mapper.UserMapper;
import com.tecnocampus.LS2.protube_back.domain.auth.UserAuthPort;
import com.tecnocampus.LS2.protube_back.domain.user.*;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Primary
public class JpaUserAuthAdapter implements UserAuthPort {

    private final UserJpaRepository repo;
    private final PasswordEncoder encoder;

    public JpaUserAuthAdapter(UserJpaRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }
/*
    @Override
    @Transactional(readOnly = true)
    public Optional<User> loadByUsername(Username username) {
        return repo.findByUsername(username.value())
                .map(this::toDomain);
    }

 */

    @Override
    @Transactional(readOnly = true)
    public Optional<User> loadByUsername(Username username) {


        Optional<UserEntity> userEntity = repo.findByUsername(username.value());

        if (userEntity.isPresent()) {
            UserEntity user = userEntity.get();
        }



        return userEntity.map(this::toDomain);
    }

    public boolean debugLogin(Username username, String rawPassword) {


        Optional<UserEntity> userEntity = repo.findByUsername(username.value());

        if (userEntity.isEmpty()) {

            return false;
        }

        UserEntity user = userEntity.get();
        String storedHash = user.getPasswordHash();



        boolean matches = encoder.matches(rawPassword, storedHash);


        return matches;
    }



    @Override
    public void save(User user) {

        repo.save(UserMapper.toEntity(user));

    }



private User toDomain(UserEntity e) {


    try {

        Role role = e.getRole(); // NO e.getRole().name()
        User user = new User(
                new UserId(e.getId()),
                new Username(e.getUsername()),
                new Password(e.getPasswordHash()),
                role
        );

        return user;
    } catch (Exception ex) {

        ex.printStackTrace();
        throw ex;
    }
}


}
