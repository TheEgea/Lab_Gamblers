package com.tecnocampus.LS2.protube_back.exception.base;

import org.springframework.http.HttpStatus;

public abstract class NotFoundException extends BusinessException {

    protected NotFoundException(String errorCode, String message) {
        super(errorCode, message);
    }

    protected NotFoundException(String errorCode, String message, Object... args) {
        super(errorCode, message, args);
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
