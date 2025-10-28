package com.tecnocampus.LS2.protube_back.exception;

import org.springframework.http.HttpStatus;

public abstract class ConflictException extends BusinessException {

    protected ConflictException(String errorCode, String message) {
        super(errorCode, message);
    }

    protected ConflictException(String errorCode, String message, Object... args) {
        super(errorCode, message, args);
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.CONFLICT;
    }
}
