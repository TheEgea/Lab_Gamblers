package com.tecnocampus.LS2.protube_back.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record VideoResponse(
        String id,
        String title,
        String description,
        Integer durationSeconds,
        String formattedDuration,
        Long sizeBytes,
        String formattedFileSize,
        String mediaUrl,
        String thumbnailUrl,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        Instant createdAt,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        Instant updatedAt
) {}
