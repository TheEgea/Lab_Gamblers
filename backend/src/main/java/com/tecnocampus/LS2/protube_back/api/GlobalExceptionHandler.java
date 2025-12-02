package com.tecnocampus.LS2.protube_back.api;

import com.tecnocampus.LS2.protube_back.exception.*;
import com.tecnocampus.LS2.protube_back.exception.user.UserAlreadyExistsException;
import com.tecnocampus.LS2.protube_back.exception.video.VideoOperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ========== Custom Business Exceptions ==========

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            NotFoundException ex, WebRequest request) {

        logger.warn("Resource not found: {} (Code: {})", ex.getMessage(), ex.getErrorCode());

        ErrorResponse error = ErrorResponse.of(
                ex.getErrorCode(),
                ex.getMessage(),
                ex.getHttpStatus().value(),
                getPath(request)
        );

        return ResponseEntity.status(ex.getHttpStatus()).body(error);
    }

    // Exeption handler UserAlreadyExistsException
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(
            UserAlreadyExistsException ex, WebRequest request) {

        logger.warn("User already exists: {}", ex.getMessage());

        ErrorResponse error = ErrorResponse.of(
                "USER_ALREADY_EXISTS",
                ex.getMessage(),
                HttpStatus.CONFLICT.value(),
                getPath(request)
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(
            ConflictException ex, WebRequest request) {

        logger.warn("Conflict: {} (Code: {})", ex.getMessage(), ex.getErrorCode());

        ErrorResponse error = ErrorResponse.of(
                ex.getErrorCode(),
                ex.getMessage(),
                ex.getHttpStatus().value(),
                getPath(request)
        );

        return ResponseEntity.status(ex.getHttpStatus()).body(error);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            ValidationException ex, WebRequest request) {

        logger.warn("Validation error: {} (Code: {})", ex.getMessage(), ex.getErrorCode());

        ErrorResponse error = ErrorResponse.of(
                ex.getErrorCode(),
                ex.getMessage(),
                ex.getHttpStatus().value(),
                getPath(request)
        );

        return ResponseEntity.status(ex.getHttpStatus()).body(error);
    }

    @ExceptionHandler(VideoOperationException.class)
    public ResponseEntity<ErrorResponse> handleVideoOperation(
            VideoOperationException ex, WebRequest request) {

        logger.error("Video operation failed: {} (Operation: {})",
                ex.getMessage(), ex.getOperation(), ex);

        ErrorResponse error = ErrorResponse.of(
                ex.getErrorCode(),
                ex.getMessage(),
                ex.getHttpStatus().value(),
                getPath(request)
        );

        return ResponseEntity.status(ex.getHttpStatus()).body(error);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(
            BusinessException ex, WebRequest request) {

        logger.warn("Business error: {} (Code: {})", ex.getMessage(), ex.getErrorCode());

        ErrorResponse error = ErrorResponse.of(
                ex.getErrorCode(),
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                getPath(request)
        );

        return ResponseEntity.badRequest().body(error);
    }

    // ========== Generic Exceptions ==========

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex, WebRequest request) {

        logger.warn("Illegal argument: {}", ex.getMessage());

        ErrorResponse error = ErrorResponse.of(
                "INVALID_ARGUMENT",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                getPath(request)
        );

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericError(
            Exception ex, WebRequest request) {

        logger.error("Unexpected error occurred", ex);

        ErrorResponse error = ErrorResponse.of(
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred. Please try again later.",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                getPath(request)
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    // ========== Helper Methods ==========

    private ErrorResponse.ValidationError mapFieldError(FieldError fieldError) {
        return new ErrorResponse.ValidationError(
                fieldError.getField(),
                fieldError.getRejectedValue(),
                fieldError.getDefaultMessage()
        );
    }

    private String getPath(WebRequest request) {
        String description = request.getDescription(false);
        return description.startsWith("uri=") ? description.substring(4) : description;
    }
}
