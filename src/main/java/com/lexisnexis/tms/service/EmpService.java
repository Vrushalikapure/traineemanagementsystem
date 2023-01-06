package com.lexisnexis.tms.service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.lexisnexis.tms.entity.User;
import com.lexisnexis.tms.entity.UserLogin;
import com.lexisnexis.tms.exception.UserAlreadyHasAccount;
import com.lexisnexis.tms.exception.UserNotFoundException;

public interface EmpService {

	public List<User> fetchAllEmpDetail() throws UserNotFoundException;

	public User getDataByUsername(String username) throws UserNotFoundException;

	public void deleteDataByUsername(String username) throws UserNotFoundException;

	public User addUser(@RequestBody User emp) throws UserAlreadyHasAccount, NoSuchAlgorithmException;

	public User updateUser(@PathVariable String username,@RequestBody User em) throws UserNotFoundException, NoSuchAlgorithmException;

	public UserLogin loginUser(@RequestBody User emp,@RequestBody UserLogin userlogin) throws UserNotFoundException ;
}
