package com.lexisnexis.tms.controller;

<<<<<<< HEAD
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
=======
import java.security.NoSuchAlgorithmException;
>>>>>>> master
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

<<<<<<< HEAD
import com.lexisnexis.tms.dto.LoginDto;
import com.lexisnexis.tms.encrypt.PasswEncrypt;
import com.lexisnexis.tms.entity.User;
import com.lexisnexis.tms.entity.UserLogin;
import com.lexisnexis.tms.entity.WorkHistory;
import com.lexisnexis.tms.exception.UserAlreadyHasAccount;
import com.lexisnexis.tms.exception.UserNotFoundException;
import com.lexisnexis.tms.repository.EmpRepo;
import com.lexisnexis.tms.repository.LoginRepo;
import com.lexisnexis.tms.response.APIResponse;
import com.lexisnexis.tms.service.EmailSenderMail;
import com.lexisnexis.tms.service.EmpService;
import com.lexisnexis.tms.service.LoginService;
import com.lexisnexis.tms.service.PdfService;
import com.lexisnexis.tms.service.UserPdfExporter;
import com.lexisnexis.tms.service.UserService;
import com.lowagie.text.DocumentException;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
=======
import com.lexisnexis.tms.encrypt.PasswEncrypt;
import com.lexisnexis.tms.entity.User;
import com.lexisnexis.tms.entity.UserLogin;
import com.lexisnexis.tms.exception.UserAlreadyHasAccount;
import com.lexisnexis.tms.exception.UserNotFoundException;
import com.lexisnexis.tms.service.EmailSenderMail;
import com.lexisnexis.tms.service.EmpService;

import jakarta.mail.MessagingException;
>>>>>>> master
import jakarta.validation.Valid;

@RestController
public class UserController {
	
	@Autowired
	EmpService empservice;
	
<<<<<<< HEAD
=======
	
	
>>>>>>> master
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
<<<<<<< HEAD
	@Autowired
	UserService userService;

	@Autowired
	EmpRepo userRepository;

	@Autowired
	PasswEncrypt passwEncrypt;
	
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
		return (List<User>) userRepository.findAll();
	}
	
	@Autowired
	LoginService loginService;
	
	@Autowired
	UserLogin loginTable;
	
	
	
	@Autowired
	LoginRepo loginTableRepo;
	
	@PostMapping("/login")
	public ResponseEntity<APIResponse> login(@RequestBody LoginDto loginDto) throws InterruptedException, NoSuchAlgorithmException{
		APIResponse apiResponse= loginService.login(loginDto);

		return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
	}

	@Autowired
	PdfService pdfService;

	@GetMapping("/report")
	public void createpdf(HttpServletResponse response) throws DocumentException, IOException {

		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";

		String headerValue = "attachment; filename=logWork_" + currentDateTime + ".pdf";

		response.setHeader(headerKey, headerValue);
		List<WorkHistory> l = pdfService.getAll();
		UserPdfExporter userPdfExporter = new UserPdfExporter(l);
		userPdfExporter.export(response);
	}
=======
>>>>>>> master
	
}
