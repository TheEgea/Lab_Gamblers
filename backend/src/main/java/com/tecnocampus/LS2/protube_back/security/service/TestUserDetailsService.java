package com.tecnocampus.LS2.protube_back.security.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Service
public class TestUserDetailsService implements UserDetailsService {
    
    // Pre-hashear la contraseña al arrancar el servicio
    private static final String ADMIN_PASSWORD_HASH = "$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Romlkwdeq6YhG8ZRR8eBC5Ne"; // admin123
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("admin".equals(username)) {
            return User.builder()
                .username("admin")
                .password(ADMIN_PASSWORD_HASH) // Usar hash pre-calculado
                .authorities(Collections.emptyList())
                .build();
        }
        throw new UsernameNotFoundException("Usuario no encontrado: " + username);
    }
}
