package com.lexisnexis.tms.serviceImpl;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.lexisnexis.tms.dto.ChangePassword;
import com.lexisnexis.tms.exception.*;
import com.lexisnexis.tms.repository.LoginRepository;
import com.lexisnexis.tms.services.UserService;
import com.lexisnexis.tms.util.PasswEncrypt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.lexisnexis.tms.entity.UserLogin;
import com.lexisnexis.tms.entity.User;
import com.lexisnexis.tms.entity.WorkHistory;
import com.lexisnexis.tms.repository.UserRepository;
import com.lexisnexis.tms.repository.WorkHistoryRepository;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	LoginRepository loginRepository;

	@Autowired
	WorkHistoryRepository workHistoryRepository;

	@Autowired
	PasswEncrypt passwEncrypt;

	final private static Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);
	@Override
	public String registerNewUser(User user) throws NoSuchAlgorithmException {
		// add check for username exists in database
		if (userRepository.existsByUserName(user.getUserName())) {
			throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Username is already exists!.");
		} else {
			userRepository.save(user);
		}
		return "User registered successfully!.";
	}

	public WorkHistory updateWorkHistory(@RequestBody WorkHistory workHistory)
			throws UserNotLoginException, UserNotFoundException {
		UserLogin user = loginRepository.findByUserName(workHistory.getUserName());
		if (user != null) {
			if (user.getLoginStatus() == false) {
				throw new UserNotLoginException("Please Login!!!");
			} else {
				LocalDateTime loginTime = user.getLoginTime();
				workHistory.setLoginAt(loginTime);
				WorkHistory work = workHistoryRepository.save(workHistory);
				return work;
			}
		}else{
			throw new UserNotFoundException("user not found for userName: " + workHistory.getUserName());
		}
	}

	@Override
	public List<User> fetchAllUserDetail() throws UserNotFoundException {
		long count=userRepository.count();
		if(count!=0)
		{
			return  (List<User>)userRepository.findAll();
		}
		else
		{
			throw new UserNotFoundException("We Don't Have Any user yet");
		}
	}

	@Override
	public User getDataByUserName(String userName) throws UserNotFoundException {
		User user=userRepository.findByUserName(userName);
		if(user!=null)
		{
			return user;
		}
		else
		{
			throw new UserNotFoundException("Usrname name does not Exist"+" "+userName);
		}
	}

	@Async
	@Override
	public CompletableFuture<User> getUserByUserName(String userName) throws UserNotFoundException{
		return CompletableFuture.completedFuture(getDataByUserName(userName));
	}

	@Override
	public void deleteDataByUserName(String userName) throws UserNotFoundException {
		User user=userRepository.findByUserName(userName);
		UserLogin userLogin=loginRepository.findByUserName(userName);
		if(user!=null)
		{
			if(userLogin!=null && userLogin.getLoginStatus()==true) {
				userRepository.deleteById(userName);
				loginRepository.deleteById(userName);
			}
			else
			{
				throw new UserNotFoundException("Else user Has not loginIn"+" "+userName);
			}
		}
		else
		{
			throw new UserNotFoundException("user has not register");
		}
	}

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
			existingUser.setPassword(passwEncrypt.encryptPass(user.getPassword()));
			userRepository.save(existingUser);
			return "Password has been update successfully";
		}
	}

	@Override
	public String changePassword(String userName, ChangePassword changePassword)
			throws UserNotLoginException, NoSuchAlgorithmException, UserPasswordDoesNotMatching, UserNotLoginExceptions {

		Optional<UserLogin> findById = loginRepository.findById(userName);
		if (!findById.isEmpty()) {
			User loginUser = userRepository.findById(userName).get();
			String dbPass = loginUser.getPassword();
			String userName2 = findById.get().getUserName();
			Boolean isLogin = findById.get().getLoginStatus();
			String oldPass = passwEncrypt.encryptPass(changePassword.getOldPassword());
			if (userName2 != null && isLogin != false) {
				if (oldPass.equals(dbPass)) {
					loginUser.setPassword(passwEncrypt.encryptPass(changePassword.getNewPassword()));
					userRepository.save(loginUser);
					LOGGER.debug("change password() completed ");
				} else {

					LOGGER.debug("change password username not found in db");
					throw new UserPasswordDoesNotMatching("User password does not matching");
				}
			} else {
				throw new UserNotLoginException("User Not login this time");
			}
		} else {
			throw new UserNotLoginExceptions("User Not login this time");
		}

		return "Password Changed successfully";

	}
}
