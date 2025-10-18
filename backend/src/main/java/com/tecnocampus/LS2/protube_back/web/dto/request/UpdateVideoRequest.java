package com.tecnocampus.LS2.protube_back.web.dto.request;

import jakarta.validation.constraints.Size;

public record UpdateVideoRequest(
        @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
        String title,

        @Size(max = 5000, message = "Description must not exceed 5000 characters")
        String description
) {
    public UpdateVideoRequest {
        title = title != null ? title.trim() : null;
        description = description != null ? description.trim() : null;

        if (title != null && title.isEmpty()) {
            title = null;
        }
        if (description != null && description.isEmpty()) {
            description = null;
        }
    }
}
