package com.lexisnexis.tms.advice;

import com.lexisnexis.tms.exception.UserPasswordDoesNotMatching;
import com.lexisnexis.tms.exception.UserNotLoginExceptions;
import com.lexisnexis.tms.exception.UserAppError;
import com.lexisnexis.tms.exception.UserNamedoesNotMatchException;
import com.lexisnexis.tms.exception.UserNameAlreadyExistException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class UserAppHandlerException {

    private static final Logger LOGGER = LogManager.getLogger(UserAppHandlerException.class);

    @ExceptionHandler(UserNamedoesNotMatchException.class)
    public ResponseEntity<String> handleUserNameNotMatchException(
            UserNamedoesNotMatchException nameDoesNotMatchException) {
        LOGGER.debug("tms UserAppHandlerException class");
        return new ResponseEntity<String>("UserName doesn't matching with database", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNameAlreadyExistException.class)
    public ResponseEntity<UserAppError> handleUserNameNotExistException(
            UserNameAlreadyExistException nameDoesNotMatchException) {
        LOGGER.debug("tms UserAppHandlerException class handleUserNameNotExistException() started");
        final UserAppError userAppError = new UserAppError(404, "UserName Already Exist with database", new Date());
        LOGGER.debug("tms UserAppHandlerException class handleUserNameNotExistException() ended");
        return new ResponseEntity<UserAppError>(userAppError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationException(MethodArgumentNotValidException exception) {
        LOGGER.debug("tms UserAppHandlerException class handleValidationException() started");
        final HashMap<String, String> errorMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        LOGGER.debug("tms UserAppHandlerException class handleValidationException() ended");
        return errorMap;
    }

    @ExceptionHandler(UserNotLoginExceptions.class)
    public ResponseEntity<UserAppError> handleUserNotLoginException(
            UserNotLoginExceptions loginException) {

        LOGGER.debug("tms UserNotLoginException class");
        final UserAppError userAppError = new UserAppError(404, "UserName doesn't Exist with database", new Date());
        return new ResponseEntity<UserAppError>(userAppError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserPasswordDoesNotMatching.class)
    public ResponseEntity<String> handleUserNotPasswordException(
            UserPasswordDoesNotMatching passwordDoesNotMatching) {
        LOGGER.debug("tms UserNotLoginException class");
        return new ResponseEntity<String>("password doesn't matching with database", HttpStatus.BAD_REQUEST);
    }
}
