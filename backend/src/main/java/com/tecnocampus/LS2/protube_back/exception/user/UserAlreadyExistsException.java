package com.tecnocampus.LS2.protube_back.exception.user;

import com.tecnocampus.LS2.protube_back.exception.ConflictException;

public class UserAlreadyExistsException extends ConflictException {

    private static final String ERROR_CODE = "USER_ALREADY_EXISTS";

    public UserAlreadyExistsException(String username) {
        super(ERROR_CODE, "User already exists with username: " + username, username);
    }

    public UserAlreadyExistsException(String message, String username) {
        super(ERROR_CODE, message, username);
    }

    public String getUsername() {
        Object[] args = getArgs();
        return args.length > 0 ? (String) args[0] : null;
    }
}
