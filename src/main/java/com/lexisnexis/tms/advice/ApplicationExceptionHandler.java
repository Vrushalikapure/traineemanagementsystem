package com.lexisnexis.tms.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.lexisnexis.tms.exception.UserAlreadyHasAccount;
import com.lexisnexis.tms.exception.UserNotFoundException;

@RestControllerAdvice
public class ApplicationExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(UserNotFoundException.class)
	public Map<String,String> handleBussinessException(UserNotFoundException error){
		HashMap<String,String> map=new HashMap<>();
		map.put("error", error.getMessage());
		return map;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String ,String> handleInvalidArgument(MethodArgumentNotValidException error){
		HashMap<String ,String> errorMap=new HashMap<>();
		error.getBindingResult().getFieldErrors().forEach(err->
		{
			errorMap.put(err.getField(),err.getDefaultMessage());
		});
		return errorMap;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(UserAlreadyHasAccount.class)
	public Map<String,String> EmployeeAlreadyHaveAccount(UserAlreadyHasAccount useraccount){
		HashMap<String,String> map=new HashMap();
		map.put("error",useraccount.getMessage());
		return map;
	}
}
