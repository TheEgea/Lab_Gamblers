package com.tecnocampus.LS2.protube_back.exception.video;

import com.tecnocampus.LS2.protube_back.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class VideoOperationException extends BusinessException {

    private static final String ERROR_CODE = "VIDEO_OPERATION_FAILED";

    public VideoOperationException(String operation, String message) {
        super(ERROR_CODE, message, operation);
    }

    public VideoOperationException(String operation, String message, Throwable cause) {
        super(ERROR_CODE, message, cause, operation);
    }

    public String getOperation() {
        Object[] args = getArgs();
        return args.length > 0 ? (String) args[0] : null;
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
