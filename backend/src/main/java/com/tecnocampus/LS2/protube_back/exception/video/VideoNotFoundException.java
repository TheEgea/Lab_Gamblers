package com.tecnocampus.LS2.protube_back.exception.video;

import com.tecnocampus.LS2.protube_back.exception.NotFoundException;

public class VideoNotFoundException extends NotFoundException {

    private static final String ERROR_CODE = "VIDEO_NOT_FOUND";

    public VideoNotFoundException(String videoId) {
        super(ERROR_CODE, "Video not found with id: " + videoId, videoId);
    }

    public VideoNotFoundException(String message, String videoId) {
        super(ERROR_CODE, message, videoId);
    }

    public String getVideoId() {
        Object[] args = getArgs();
        return args.length > 0 ? (String) args[0] : null;
    }
}
