package com.lexisnexis.tms.exception;

@SuppressWarnings("serial")
public class UserNotFoundException extends Exception{

    public UserNotFoundException(String message) {
        super(message);
    }
}
