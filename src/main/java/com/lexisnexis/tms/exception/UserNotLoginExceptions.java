package com.lexisnexis.tms.exception;

public class UserNotLoginExceptions extends RuntimeException {
    static final long serialVersionUID = 1L;

    public UserNotLoginExceptions(String msg) {
        super(msg);
    }
}
