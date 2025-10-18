package com.tecnocampus.LS2.protube_back.web.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateVideoRequest(
        @NotBlank(message = "Title is required")
        @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
        String title,

        @Size(max = 5000, message = "Description must not exceed 5000 characters")
        String description
) {
    public CreateVideoRequest {
        // Trim whitespace
        title = title != null ? title.trim() : null;
        description = description != null ? description.trim() : null;

        // Convert empty description to null
        if (description != null && description.isEmpty()) {
            description = null;
        }
    }
}
