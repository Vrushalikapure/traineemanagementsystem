package com.lexisnexis.tms.advice;

import com.lexisnexis.tms.exception.BlogAPIException;
import com.lexisnexis.tms.exception.UserAlreadyHasAccount;
import com.lexisnexis.tms.exception.UserNotFoundException;
import com.lexisnexis.tms.exception.UserNotLoginException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException methodArgumentNotValidException) {
        final Map<String, String> errorMap = new ConcurrentHashMap<>();
        methodArgumentNotValidException.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        return errorMap;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(UserNotFoundException.class)
    public Map<String, String> handleBusinessException(UserNotFoundException userNotFoundException) {
        final Map<String, String> errorMap = new ConcurrentHashMap<>();
        errorMap.put("errorMessage", userNotFoundException.getMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BlogAPIException.class)
    public Map<String, String> handleBlogApiException(BlogAPIException blogAPIException) {
        final Map<String, String> errorMap = new ConcurrentHashMap<>();
        errorMap.put("errorMessage", blogAPIException.getMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserAlreadyHasAccount.class)
    public Map<String, String> employeeAlreadyHaveAccount(UserAlreadyHasAccount userAccount) {
        final ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
        map.put("error", userAccount.getMessage());
        return map;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(UserNotLoginException.class)
    public Map<String, String> userNotLogin(UserNotLoginException userNotLoginException) {
        final Map<String, String> errorMap = new ConcurrentHashMap<>();
        errorMap.put("errorMessage", userNotLoginException.getMessage());
        return errorMap;
    }
}