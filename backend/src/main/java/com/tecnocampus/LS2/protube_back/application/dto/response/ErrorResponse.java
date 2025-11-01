package com.tecnocampus.LS2.protube_back.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        String error,
        String message,
        int status,
        String path,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        Instant timestamp,

        List<ValidationError> validationErrors
) {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ValidationError(
            String field,
            Object rejectedValue,
            String message
    ) {}

    public static ErrorResponse of(String error, String message, int status, String path) {
        return new ErrorResponse(error, message, status, path, Instant.now(), null);
    }

    public static ErrorResponse validation(String message, int status, String path, List<ValidationError> validationErrors) {
        return new ErrorResponse("VALIDATION_FAILED", message, status, path, Instant.now(), validationErrors);
    }
}
