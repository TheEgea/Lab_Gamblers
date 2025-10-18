package com.tecnocampus.LS2.protube_back.exception.video;

import com.tecnocampus.LS2.protube_back.exception.base.ConflictException;

public class VideoDuplicateException extends ConflictException {

    private static final String ERROR_CODE = "VIDEO_DUPLICATE";

    public VideoDuplicateException(String checksum) {
        super(ERROR_CODE, "Video already exists with checksum: " + checksum, checksum);
    }

    public VideoDuplicateException(String message, String checksum) {
        super(ERROR_CODE, message, checksum);
    }

    public String getChecksum() {
        Object[] args = getArgs();
        return args.length > 0 ? (String) args[0] : null;
    }
}
