package com.lexisnexis.tms.rest;

import java.security.NoSuchAlgorithmException;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lexisnexis.tms.dto.ChangePassword;
import com.lexisnexis.tms.entity.User;
import com.lexisnexis.tms.exception.UserNotLoginException;
import com.lexisnexis.tms.exception.UserPasswordDoesNotMatching;
import com.lexisnexis.tms.service.UserService;

@RestController
@RequestMapping("/tms/api/v1/")
public class TmsController {
	
	final private static Logger LOGGER = LogManager.getLogger(TmsController.class);
	
	@Autowired
	private UserService userService;

		@PostMapping(value = "/updateUser")
	public ResponseEntity<String> updateUser(@RequestBody User userUpdate) {
		String updateUser = userService.updateUser(userUpdate);
		return new ResponseEntity<>(updateUser, HttpStatus.ACCEPTED);
	}

	@PostMapping("/forgotpassword")
	public ResponseEntity<String> forgotPassword(@RequestBody User user) throws NoSuchAlgorithmException {
		String forgotPassword = userService.forgotPassword(user);
		return new ResponseEntity<String>(forgotPassword, HttpStatus.OK);
	}
	
	@PostMapping("/changepassword")
	public ResponseEntity<String> changePassword(@RequestBody ChangePassword changePassword)
			throws NoSuchAlgorithmException, UserNotLoginException, UserPasswordDoesNotMatching {
		String user="sam10";
		String password = userService.changePassword(user,changePassword);
		return new ResponseEntity<String>(password, HttpStatus.OK);
	}
	
}
