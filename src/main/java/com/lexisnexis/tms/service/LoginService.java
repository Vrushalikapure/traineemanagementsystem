package com.lexisnexis.tms.service;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lexisnexis.tms.dto.LoginDto;
import com.lexisnexis.tms.encrypt.PasswEncrypt;
import com.lexisnexis.tms.entity.User;
import com.lexisnexis.tms.entity.UserLogin;
import com.lexisnexis.tms.repository.EmpRepo;
import com.lexisnexis.tms.repository.LoginRepo;
import com.lexisnexis.tms.response.APIResponse;



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
	EmpRepo userRepository;

	@Autowired
	UserLogin loginTable;

	@Autowired
	LoginRepo loginTableRepo;
	
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
			if (loginTable.getFailureAttempts() < mAX_ATTEMPTS) {
				int attempts = loginTable.getFailureAttempts() ;
				loginTable.setFailureAttempts(++attempts);				
				loginTable.setIsLocked(Boolean.FALSE);

			}
			if (loginTable.getLockTime() == null && loginTable.getFailureAttempts() == 4) {

				loginTable.setIsLocked(Boolean.TRUE);
				loginTable.setLockTime(new Date());
//			
//				Date date = loginTable.getLockTime();
//			
				apiResponse.setData("User Locked wait for 5 minutes");
				return apiResponse;

			}
//			

			apiResponse.setData("User login failed");
			loginTable.setUserName(loginDto.getUserName());

			loginTable.setLoginStatus(Boolean.FALSE);
			loginTable.onSave();

			loginTableRepo.save(loginTable);

			return apiResponse;
		}
		if (loginTable.getIsLocked()) {
			long lockTimeInMillis = loginTable.getLockTime().getTime();
			long currentTimeInMillis = System.currentTimeMillis();

			if (((lockTimeInMillis / lockTime) * lockTime + lockTime) < currentTimeInMillis) {
				System.out.println("aaaaaaaaaaaaa--" + (lockTimeInMillis / lockTime) * lockTime + lockTime);
				System.out.println("aaaaaaaaaaaaa--" + currentTimeInMillis);
//	        	user1.setLockTime(null);
				loginTable.setFailureAttempts(0);
				loginTable.setIsLocked(Boolean.FALSE);

				loginTable.setLockTime(null);

			}
		}

		if (!loginTable.getIsLocked()) {
			apiResponse.setData("User logged in");

			username = user1.getUserName();

			loginTable.setIsLocked(Boolean.FALSE);
			loginTable.setUserName(loginDto.getUserName());

			loginTable.onSave();
			loginTable.setLoginStatus(Boolean.TRUE);

			loginTable.setFailureAttempts(0);
			
//			

			loginTableRepo.save(loginTable);
		} else {
			apiResponse.setData("Error user is locked");
			return apiResponse;
		}

		return apiResponse;

	}

}