package com.lexisnexis.tms.service;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

import com.lexisnexis.tms.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lexisnexis.tms.dto.LoginDto;


import com.lexisnexis.tms.entity.User;
import com.lexisnexis.tms.entity.UserLogin;
import com.lexisnexis.tms.repository.LoginRepository;
import com.lexisnexis.tms.repository.UserRepository;
import com.lexisnexis.tms.response.APIResponse;
import com.lexisnexis.tms.util.PasswEncrypt;


@Service
@Transactional
public class LoginService {

	public static final int MAX_FAILED_ATTEMPTS = 3;

//	private static final long LOCK_TIME_DURATION = 15 * 60 * 1000;

	@Autowired
	User user;

	@Autowired
	LoginDto loginDto;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserLogin userLogin;

	@Autowired
	LoginRepository loginRepository;
	
	@Autowired
	PasswEncrypt passwEncrypt;

//	int count = 0;
	int mAX_ATTEMPTS = 4;

	long lockTime = 05 * 60 * 1000;
	String username;
	

	public APIResponse login(LoginDto loginDto) throws InterruptedException, NoSuchAlgorithmException {
		APIResponse apiResponse = new APIResponse();

		User user1 = userRepository.findByUserName(loginDto.getUserName());
//	
		String password=passwEncrypt.encryptPass(loginDto.getPassword());
		if (user1 == null) {
			apiResponse.setData("User not exists please Register");
			return apiResponse;
//			passwEncrypt.encryptPass(user.getPassword())
				
		} else if (!user1.getPassword().equals(password)) {
			if (userLogin.getFailureAttempts() < mAX_ATTEMPTS) {
				int attempts = userLogin.getFailureAttempts() ;
				userLogin.setFailureAttempts(++attempts);
				userLogin.setIsLocked(Boolean.FALSE);

			}
			if (userLogin.getLockTime() == null && userLogin.getFailureAttempts() == 4) {

				userLogin.setIsLocked(Boolean.TRUE);
				userLogin.setLockTime(new Date());
//			
//				Date date = userLogin.getLockTime();
//			
				apiResponse.setData("User Locked wait for 5 minutes");
				return apiResponse;

			}
//			

			apiResponse.setData("User login failed");
			userLogin.setUserName(loginDto.getUserName());

			userLogin.setLoginStatus(Boolean.FALSE);
			userLogin.onSave();

			loginRepository.save(userLogin);

			return apiResponse;
		}


		if (user1!=null || !userLogin.getIsLocked()) {
			apiResponse.setData("User logged in");

			username = user1.getUserName();

			userLogin.setIsLocked(Boolean.FALSE);
			userLogin.setUserName(loginDto.getUserName());

			userLogin.onSave();
			userLogin.setLoginStatus(Boolean.TRUE);

			userLogin.setFailureAttempts(0);
			
//			

			loginRepository.save(userLogin);
		}

		if (userLogin.getIsLocked()) {
			long lockTimeInMillis = userLogin.getLockTime().getTime();
			long currentTimeInMillis = System.currentTimeMillis();

			if (((lockTimeInMillis / lockTime) * lockTime + lockTime) < currentTimeInMillis) {
//				System.out.println("aaaaaaaaaaaaa--" + (lockTimeInMillis / lockTime) * lockTime + lockTime);
//				System.out.println("aaaaaaaaaaaaa--" + currentTimeInMillis);
//	        	user1.setLockTime(null);
				userLogin.setFailureAttempts(0);
				userLogin.setIsLocked(Boolean.FALSE);

				userLogin.setLockTime(null);

			}
		}

		return apiResponse;

	}

}