package com.tecnocampus.LS2.protube_back.response;

import com.tecnocampus.LS2.protube_back.application.dto.response.ErrorResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

    class ErrorResponseTest {

        @Test
        void testOfMethod() {
            String error = "NOT_FOUND";
            String message = "Resource not found";
            int status = 404;
            String path = "/api/resource";

            ErrorResponse response = ErrorResponse.of(error, message, status, path);

            assertNotNull(response);
            assertEquals(error, response.error());
            assertEquals(message, response.message());
            assertEquals(status, response.status());
            assertEquals(path, response.path());
            assertNotNull(response.timestamp());
            assertNull(response.validationErrors());
        }

        @Test
        void testValidationMethod() {
            String message = "Validation failed";
            int status = 400;
            String path = "/api/resource";
            List<ErrorResponse.ValidationError> validationErrors = List.of(
                    new ErrorResponse.ValidationError("field1", "value1", "must not be null"),
                    new ErrorResponse.ValidationError("field2", "value2", "must be a valid email")
            );

            ErrorResponse response = ErrorResponse.validation(message, status, path, validationErrors);

            assertNotNull(response);
            assertEquals("VALIDATION_FAILED", response.error());
            assertEquals(message, response.message());
            assertEquals(status, response.status());
            assertEquals(path, response.path());
            assertNotNull(response.timestamp());
            assertEquals(validationErrors, response.validationErrors());
        }

        @Test
        void testValidationError() {
            String field = "field1";
            Object rejectedValue = "value1";
            String message = "must not be null";

            ErrorResponse.ValidationError validationError = new ErrorResponse.ValidationError(field, rejectedValue, message);

            assertNotNull(validationError);
            assertEquals(field, validationError.field());
            assertEquals(rejectedValue, validationError.rejectedValue());
            assertEquals(message, validationError.message());
        }
    }