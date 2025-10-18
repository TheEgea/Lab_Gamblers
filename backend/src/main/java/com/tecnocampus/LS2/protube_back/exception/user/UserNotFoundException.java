package com.tecnocampus.LS2.protube_back.exception.user;

import com.tecnocampus.LS2.protube_back.exception.base.NotFoundException;

public class UserNotFoundException extends NotFoundException {

    private static final String ERROR_CODE = "USER_NOT_FOUND";

    public UserNotFoundException(String username) {
        super(ERROR_CODE, "User not found with username: " + username, username);
    }

    public UserNotFoundException(String message, String username) {
        super(ERROR_CODE, message, username);
    }

    public String getUsername() {
        Object[] args = getArgs();
        return args.length > 0 ? (String) args[0] : null;
    }
}
