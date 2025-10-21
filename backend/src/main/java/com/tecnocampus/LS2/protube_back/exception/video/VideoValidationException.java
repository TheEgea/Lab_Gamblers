package com.tecnocampus.LS2.protube_back.exception.video;

import com.tecnocampus.LS2.protube_back.exception.ValidationException;

public class VideoValidationException extends ValidationException {

    private static final String ERROR_CODE = "VIDEO_VALIDATION_FAILED";

    public VideoValidationException(String message) {
        super(ERROR_CODE, message);
    }

    public VideoValidationException(String field, String message) {
        super(ERROR_CODE, message, field);
    }

    public String getField() {
        Object[] args = getArgs();
        return args.length > 0 ? (String) args[0] : null;
    }
}
