package com.lexisnexis.tms.controller;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.lexisnexis.tms.encrypt.PasswEncrypt;
import com.lexisnexis.tms.entity.User;
import com.lexisnexis.tms.entity.UserLogin;
import com.lexisnexis.tms.exception.UserAlreadyHasAccount;
import com.lexisnexis.tms.exception.UserNotFoundException;
import com.lexisnexis.tms.service.EmailSenderMail;
import com.lexisnexis.tms.service.EmpService;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

@RestController
public class UserController {
	
	@Autowired
	EmpService empservice;
	
	
	
	@GetMapping("/home")
	public String page() {
		return "This is home page";
	}

	@GetMapping("/getAllUserDetails")
	public ResponseEntity<List<User>>  fetchAllEmpDetail()throws UserNotFoundException{
		return new ResponseEntity<>(this.empservice.fetchAllEmpDetail(),HttpStatus.ACCEPTED);
	}

	@GetMapping("/getDataByUsername/{username}")
	public ResponseEntity<User> getData(@PathVariable String username) throws UserNotFoundException {
		return new ResponseEntity<>(empservice.getDataByUsername(username),HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteDataByUsername/{username}")
	public String deleteDataByUsername(@PathVariable String username) throws UserNotFoundException
	{
		empservice.deleteDataByUsername(username);	
		return "User removed successfully "+username;
	}

	@PostMapping("/addUser")
	public String addUser(@RequestBody @Valid User emp) throws UserAlreadyHasAccount, NoSuchAlgorithmException {
		empservice.addUser(emp);
		return "User Added";
	}
	
	@PutMapping("/UpdateUser/{username}")
	public String updateUser(@PathVariable String username,@RequestBody User em) throws UserNotFoundException, NoSuchAlgorithmException {
		 empservice.updateUser(username, em);
		 return "User Record UpDated Properly";
	}	
	
	@PostMapping("/loginUser")
	public String loginUser(@RequestBody User emp,@RequestBody UserLogin userlogin) throws UserNotFoundException {
		empservice.loginUser(emp,userlogin);
		return "Login SuccessFully";
	}
	
}
