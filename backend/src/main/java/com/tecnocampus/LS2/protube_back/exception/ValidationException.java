package com.tecnocampus.LS2.protube_back.exception;

import org.springframework.http.HttpStatus;

public abstract class ValidationException extends BusinessException {

    protected ValidationException(String errorCode, String message) {
        super(errorCode, message);
    }

    protected ValidationException(String errorCode, String message, Object... args) {
        super(errorCode, message, args);
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
