package com.lexisnexis.tms.service;

import java.security.NoSuchAlgorithmException;

import com.lexisnexis.tms.dto.ChangePassword;
import com.lexisnexis.tms.entity.User;
import com.lexisnexis.tms.exception.UserNotLoginException;
import com.lexisnexis.tms.exception.UserPasswordDoesNotMatching;

public interface UserService {



	public String updateUser(User user);

	public String forgotPassword(User user) throws NoSuchAlgorithmException;

	public String changePassword(String user, ChangePassword changePassword)
			throws UserNotLoginException, NoSuchAlgorithmException, UserPasswordDoesNotMatching;

}
