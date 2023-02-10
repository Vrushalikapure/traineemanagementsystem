package com.lexisnexis.tms.exception;

@SuppressWarnings("serial")
public class UserAlreadyHasAccount extends Exception {

    public UserAlreadyHasAccount(String message) {
        super(message);
    }
}
