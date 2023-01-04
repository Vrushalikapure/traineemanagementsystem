package com.lexisnexis.tms.service;

import java.security.NoSuchAlgorithmException;

import com.lexisnexis.tms.entity.User;

public interface UserService {
	
	public User saveUser(User user) throws NoSuchAlgorithmException;
	public void updateUser(User user,String userName);
}
