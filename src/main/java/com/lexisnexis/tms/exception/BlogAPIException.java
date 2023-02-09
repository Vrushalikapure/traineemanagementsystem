package com.lexisnexis.tms.exception;

import org.springframework.http.HttpStatus;

public class BlogAPIException extends RuntimeException {

    static final long serialVersionUID = 1L;

    final private HttpStatus status;
    final private String message;

    public BlogAPIException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public BlogAPIException(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
