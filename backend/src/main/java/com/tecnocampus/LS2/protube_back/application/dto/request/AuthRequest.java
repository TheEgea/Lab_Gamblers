package com.tecnocampus.LS2.protube_back.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AuthRequest(
        @NotBlank(message = "username obligatorio")
        @Size(min = 3, max = 20, message = "username entre 3 y 20 caracteres")
        @Pattern(regexp = "^[A-Za-z0-9._-]+$", message = "username sólo letras, números, '.', '_' o '-'")
        String username,

        @NotBlank(message = "password obligatorio")
        @Size(min = 8, message = "la contraseña debe tener al menos 8 caracteres")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
                message = "la contraseña debe contener mayúscula, minúscula, número y carácter especial")
        String password,


        @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "email no válido")
        String email
) {}