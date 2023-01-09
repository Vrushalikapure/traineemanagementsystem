package com.lexisnexis.tms.service;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lexisnexis.tms.dto.ChangePassword;
import com.lexisnexis.tms.entity.User;
import com.lexisnexis.tms.entity.UserLogin;
import com.lexisnexis.tms.exception.UserNameAlreadyExistException;
import com.lexisnexis.tms.exception.UserNamedoesNotMatchException;
import com.lexisnexis.tms.exception.UserNotLoginException;
import com.lexisnexis.tms.exception.UserPasswordDoesNotMatching;
import com.lexisnexis.tms.repository.LoginRepository;
import com.lexisnexis.tms.repository.UserRepository;
import com.lexisnexis.tms.utils.PasswordEncrpt;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private LoginRepository loginRepository;
	 
	@Autowired
	private PasswordEncrpt passwordEncrpt;
	
	final private static Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);
	

	@Override
	public String updateUser(User user) {
		Optional<User> findedUser = userRepository.findById(user.getUserName());
		boolean present = findedUser.isPresent();
		if (present == true) {
			User newUser = findedUser.get();
			newUser.setFirstName(user.getFirstName());
			newUser.setLastName(user.getLastName());
			newUser.setEmail(user.getEmail());
			newUser.setLocation(user.getLocation());
			newUser.setMobileNo(user.getMobileNo());
			userRepository.save(newUser);
			return "User Data updated successfully";
		} else {
			throw new UserNamedoesNotMatchException();
		}
	}

	@Override
	public String forgotPassword(User user) throws NoSuchAlgorithmException {
		User existingUser = userRepository.findById(user.getUserName()).orElse(null);
		if (existingUser == null) {
			throw new UserNamedoesNotMatchException();
		} else {
			existingUser.setUserName(user.getUserName());
			existingUser.setPassword(passwordEncrpt.encryptPass(user.getPassword()));
			userRepository.save(existingUser);
			return "Password has been update successfully";
		}
	}

	@Override
	public String changePassword(String user,ChangePassword changePassword) throws UserNotLoginException, NoSuchAlgorithmException, UserPasswordDoesNotMatching {		
		Optional<UserLogin> findById = loginRepository.findById(user);
	
		User user2 = userRepository.findById(user).get();
		String dbPass = user2.getPassword();
		
		String isLogin = findById.get().getLoginStatus();
		String newpass = passwordEncrpt.encryptPass(changePassword.getOldPassword());
		
		
		if(isLogin.equals("N"))
		 {
			throw new UserNotLoginException("User Not login this time");
		}
		else {
			
			if(newpass.equals(dbPass)) {
				
				user2.setPassword(passwordEncrpt.encryptPass(changePassword.getNewPassword()));
				userRepository.save(user2);
				LOGGER.debug("tms ServiceIml class change password() completed ");
			
			}
			else {
				
				throw new UserPasswordDoesNotMatching("User password does not matching");
				
			}
			
		}
		return "Password Changed successfully";
	}
	
	
}