package com.lexisnexis.tms.advice;

//import org.apache.logging.log4j.LogManager;
import com.lexisnexis.tms.exception.*;
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
//import java.util.logging.Logger;

@RestControllerAdvice
public class UserAppHandlerException {

    final private static Logger LOGGER = LogManager.getLogger(UserAppHandlerException.class);

    @ExceptionHandler(value = UserNamedoesNotMatchException.class)
    public ResponseEntity<String> handelUserNameNotMatchException(
            UserNamedoesNotMatchException namedoesNotMatchException) {
        LOGGER.debug("tms UserAppHandlerException class");
        return new ResponseEntity<String>("UserName doesn't matching with database", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UserNameAlreadyExistException.class)
    public ResponseEntity<UserAppError> handelUserNameNotExistException(
            UserNameAlreadyExistException namedoesNotMatchException) {
        LOGGER.debug("tms UserAppHandlerException class handelUserNameNotExistException() started");
        UserAppError userAppError = new UserAppError(404, "UserName Already Exist with database", new Date());
        LOGGER.debug("tms UserAppHandlerException class handelUserNameNotExistException() ended");
        return new ResponseEntity<UserAppError>(userAppError, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Map<String,String> handleValidationException(MethodArgumentNotValidException exception){
        LOGGER.debug("tms UserAppHandlerException class handleValidationException() started");
        HashMap<String, String> errorMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        LOGGER.debug("tms UserAppHandlerException class handleValidationException() ended");
        return errorMap;
    }

    @ExceptionHandler(value = UserNotLoginExceptions.class)
    public ResponseEntity<UserAppError> handelUserNotLoginException(
            UserNotLoginExceptions loginException) {
    	
        LOGGER.debug("tms UserNotLoginException class");
        UserAppError userAppError = new UserAppError(404, "UserName doesn't Exist with database", new Date());
        return new ResponseEntity<UserAppError>(userAppError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = UserPasswordDoesNotMatching.class)
    public ResponseEntity<String> handelUserNotPasswordException(
            UserPasswordDoesNotMatching passwordDoesNotMatching) {
        LOGGER.debug("tms UserNotLoginException class");
        return new ResponseEntity<String>("password doesn't matching with database", HttpStatus.BAD_REQUEST);
    }
}
