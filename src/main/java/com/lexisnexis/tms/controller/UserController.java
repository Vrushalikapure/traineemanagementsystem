package com.lexisnexis.tms.controller;

import com.lexisnexis.tms.entity.User;
import com.lexisnexis.tms.entity.WorkHistory;
import com.lexisnexis.tms.exception.UserNotFoundException;
import com.lexisnexis.tms.repository.UserRepository;
import com.lexisnexis.tms.service.UserService;
import com.lexisnexis.tms.util.PasswEncrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/tms/api/v1")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswEncrypt passwEncrypt;

	// Register api
	@PostMapping("/register")
	public String registerNewUser(@RequestBody @Valid User user) throws NoSuchAlgorithmException {

		user.setPassword(passwEncrypt.encryptPass(user.getPassword()));
		userService.registerNewUser(user);
		return "user registration successfully";
	}

	@PostMapping("/workHistory")
	public String saveWorkHistory(@RequestBody WorkHistory workHistory) {

		userService.updateWorkHistory(workHistory);
		return "work history updated";

	}

	@GetMapping("/getUser")
	public User getUserDetail(String userName) throws UserNotFoundException {
		User user = userService.getUserDetails(userName);
		return user;
	}

//	@GetMapping("/getDataByUserName/{userName}")
//	public ResponseEntity<User> getData(@PathVariable String userName) throws UserNotFoundException {
//		return new ResponseEntity<>(UserService.getDataByUserName(userName), HttpStatus.OK);
//	}

	@GetMapping("/getAllUsers")
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

}
