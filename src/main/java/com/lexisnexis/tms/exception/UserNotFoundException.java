package com.lexisnexis.tms.exception;

public class UserNotFoundException extends RuntimeException {

    static final long serialVersionUID = 1L;

    public UserNotFoundException(String msg) {
        super(msg);
    }
}
