package com.lexisnexis.tms.rest;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lexisnexis.tms.entity.User;
import com.lexisnexis.tms.service.UserService;

@RestController
@RequestMapping("/tms/api/v1/")
public class TmsController {

	@Autowired
	private UserService userService;
	
 
	@PostMapping("/saveuser")
	public ResponseEntity<User> saveUserData(@RequestBody User user,BindingResult rs) throws NoSuchAlgorithmException {
		return new ResponseEntity<User>(userService.saveUser(user),HttpStatus.ACCEPTED);
	}
	
	@PatchMapping(value = "/update/{userName}")
	public String getUser( @PathVariable String userName, @RequestBody User userUpdate)  {
		if (!userName.isEmpty()) {
			userService.updateUser(userUpdate,userName);
			return "Record has been updated successfully";
		} 
		
		return null;
	}
}
