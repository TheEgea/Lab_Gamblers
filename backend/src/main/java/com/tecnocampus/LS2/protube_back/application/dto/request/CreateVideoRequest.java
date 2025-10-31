package com.tecnocampus.LS2.protube_back.application.dto.mapper.request;

import com.tecnocampus.LS2.protube_back.domain.video.atributes.Comentario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.List;

public record CreateVideoRequest(
        @NotBlank(message = "Title is required")
        @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
        String title,

        @Size(max = 5000, message = "Description must not exceed 5000 characters")
        String description,

        String id,
        String jsonId,
        int width,
        int height,
        Integer durationSeconds, // null si desconocido
        String user,
        List<Instant> timestamp,
        List<String> categories,
        List<String> tags,
        int viewCount,
        int likeCount,
        String channel,
        List<Comentario> comments,
        String mediaPath,        // ruta relativa/clave de almacenamiento
        String thumbnailPath
) {
    public CreateVideoRequest {
        title = title != null ? title.trim() : null;
        description = description != null ? description.trim() : null;

        if (description != null && description.isEmpty()) {
            description = null;
        }


    }
}
