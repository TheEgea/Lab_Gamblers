package com.tecnocampus.LS2.protube_back.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tecnocampus.LS2.protube_back.domain.video.atributes.Comentario;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record VideoResponse(
        String id,
        String jsonId,
        int width,
        int height,
        Integer durationSeconds, // null si desconocido
        String title,
        String user,
        List<Instant> timestamp,
        String description,
        List<String> categories,
        List<String> tags,
        int viewCount,
        int likeCount,
        String channel,
        List<Comentario> comments,
        String mediaPath,        // ruta relativa/clave de almacenamiento
        String thumbnailPath,    // opcional

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        Instant createdAt,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        Instant updatedAt
) {}
