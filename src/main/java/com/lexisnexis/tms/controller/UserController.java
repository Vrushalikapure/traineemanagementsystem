package com.lexisnexis.tms.controller;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import com.lexisnexis.tms.dto.ChangePassword;
import com.lexisnexis.tms.exception.UserNotLoginException;
import com.lexisnexis.tms.exception.UserPasswordDoesNotMatching;
import com.lexisnexis.tms.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.lexisnexis.tms.dto.LoginDto;
import com.lexisnexis.tms.entity.User;
import com.lexisnexis.tms.entity.UserLogin;
import com.lexisnexis.tms.entity.WorkHistory;
import com.lexisnexis.tms.exception.UserNotFoundException;
import com.lexisnexis.tms.repository.LoginRepository;
import com.lexisnexis.tms.repository.UserRepository;
import com.lexisnexis.tms.response.APIResponse;
import com.lexisnexis.tms.service.LoginService;
import com.lexisnexis.tms.service.PdfService;
import com.lexisnexis.tms.service.UserPdfExporter;
import com.lexisnexis.tms.service.UserService;
import com.lexisnexis.tms.util.PasswEncrypt;
import com.lowagie.text.DocumentException;

@RestController
@RequestMapping("/tms/api/v1")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswEncrypt passwEncrypt;

	@Autowired
	LoginService loginService;

	@Autowired
	LoginRepository loginRepository;

	@Autowired
	PdfService pdfService;

	@Autowired
	LoginDto loginDto;


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

	@GetMapping("/getAllUsers")
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@PostMapping("/login")
	public ResponseEntity<APIResponse> login(@RequestBody LoginDto loginDto) throws InterruptedException, NoSuchAlgorithmException{
		APIResponse apiResponse= loginService.login(loginDto);


		return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
	}

	@GetMapping("/users/report")
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

	@GetMapping("/getAllUserDetails")
	public ResponseEntity<List<User>>  fetchAllEmpDetail()throws UserNotFoundException{
		return new ResponseEntity<>(this.userService.fetchAllUserDetail(),HttpStatus.ACCEPTED);
	}

	@GetMapping("/getDataByUsername/{userName}")
	public ResponseEntity<User> getData(@PathVariable String userName) throws UserNotFoundException {
		return new ResponseEntity<>(userService.getDataByUserName(userName), HttpStatus.OK);
	}

	@DeleteMapping("/deleteDataByUsername/{userName}")
	public String deleteDataByUserName(@PathVariable String userName) throws UserNotFoundException
	{
		userService.deleteDataByUserName(userName);
		return "User removed successfully "+userName;
	}

	@PostMapping(value = "/updateUser")
	public ResponseEntity<String> updateUser(@RequestBody User userUpdate) {
		String updateUser = userService.updateUser(userUpdate);
		return new ResponseEntity<>(updateUser, HttpStatus.ACCEPTED);
	}

	@PostMapping("/forgotPassword")
	public ResponseEntity<String> forgotPassword(@RequestBody User user) throws NoSuchAlgorithmException {
		String forgotPassword = userService.forgotPassword(user);
		return new ResponseEntity<String>(forgotPassword, HttpStatus.OK);
	}

	@PostMapping("/changePassword/{userName}")
	public ResponseEntity<String> changePassword(@PathVariable String userName, @RequestBody ChangePassword changePassword)
			throws NoSuchAlgorithmException, UserNotLoginException, UserPasswordDoesNotMatching {
		//String userName="priyanka8";
		String password = userService.changePassword(userName,changePassword);
		return new ResponseEntity<String>(password, HttpStatus.OK);
	}
}
