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
        System.out.println("=== BUSCANDO USUARIO ===");
        System.out.println("Username buscado: " + username.value());

        Optional<UserEntity> userEntity = repo.findByUsername(username.value());

        if (userEntity.isPresent()) {
            UserEntity user = userEntity.get();
            System.out.println("✅ Usuario ENCONTRADO!");
            System.out.println("- Username: " + user.getUsername());
            System.out.println("- Hash guardado: " + user.getPasswordHash());
            System.out.println("- Role: " + user.getRole());
        } else {
            System.out.println("❌ Usuario NO ENCONTRADO en la DB!");
        }

        return userEntity.map(this::toDomain);
    }

    public boolean debugLogin(Username username, String rawPassword) {
        System.out.println("\n=== LOGIN DEBUG ===");
        System.out.println("Usuario: " + username.value());
        System.out.println("Password raw: '" + rawPassword + "'");
        System.out.println("Encoder type: " + encoder.getClass().getSimpleName());

        Optional<UserEntity> userEntity = repo.findByUsername(username.value());

        if (userEntity.isEmpty()) {
            System.out.println("❌ LOGIN: Usuario no existe");
            return false;
        }

        UserEntity user = userEntity.get();
        String storedHash = user.getPasswordHash();

        System.out.println("Hash en DB: " + storedHash);

        boolean matches = encoder.matches(rawPassword, storedHash);
        System.out.println("¿Password coincide? " + matches);

        return matches;
    }



    @Override
    public void save(User user) {

        repo.save(UserMapper.toEntity(user));

    }


    @Transactional(readOnly = true)
    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        return encoder.matches(rawPassword, hashedPassword);
    }
/*
    private User toDomain(UserEntity e) {
        Role role = Role.valueOf(e.getRole().name());
        return new User(new UserId(e.getId()), new Username(e.getUsername()), new Password(e.getPasswordHash()),role);
    }

 */
private User toDomain(UserEntity e) {
    System.out.println("=== MAPPING A DOMINIO ===");
    System.out.println("Entity role: " + e.getRole());

    try {
        // ❌ ARREGLAR ESTO - NO usar .name()
        Role role = e.getRole(); // NO e.getRole().name()
        User user = new User(
                new UserId(e.getId()),
                new Username(e.getUsername()),
                new Password(e.getPasswordHash()),
                role
        );
        System.out.println("✅ Mapping OK - Domain user creado");
        return user;
    } catch (Exception ex) {
        System.out.println("❌ ERROR en mapping: " + ex.getMessage());
        ex.printStackTrace();
        throw ex;
    }
}
    public void showAllUsers() {
        System.out.println("\n=== USUARIOS EN LA DB ===");
        List<UserEntity> allUsers = repo.findAll();
        System.out.println("Total usuarios: " + allUsers.size());

        for (UserEntity user : allUsers) {
            System.out.println("- Usuario: " + user.getUsername() +
                    ", Hash: " + user.getPasswordHash().substring(0, Math.min(15, user.getPasswordHash().length())) + "..." +
                    ", Role: " + user.getRole());
        }
    }

}
