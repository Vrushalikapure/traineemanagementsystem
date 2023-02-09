package com.lexisnexis.tms.serviceimpl;

import com.lexisnexis.tms.dto.LoginDto;
import com.lexisnexis.tms.entity.UserEntity;
import com.lexisnexis.tms.entity.UserLogin;
import com.lexisnexis.tms.repository.LoginRepository;
import com.lexisnexis.tms.repository.UserRepository;
import com.lexisnexis.tms.response.APIResponse;
import com.lexisnexis.tms.services.LoginService;
import com.lexisnexis.tms.util.PasswEncrypt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Service
public class LoginServiceImpl implements LoginService {

    public static final int MAX_FAILED_ATTEMPTS = 3;
    private static final Logger LOGGER = LogManager.getLogger(LoginServiceImpl.class);
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserLogin userLogin;
    @Autowired
    LoginRepository loginRepository;
    @Autowired
    PasswEncrypt passwEncrypt;

    int maxAttempts = 4;

    long lockTime = 05 * 60 * 1000;
    String username;

    @Override
    public APIResponse login(LoginDto loginDto) throws NoSuchAlgorithmException {
        final APIResponse apiResponse = new APIResponse();
        final UserEntity user1 = userRepository.findByUserName(loginDto.getUserName());
        LOGGER.debug("-------------------" + user1 + "----------------" + userLogin.getUserName() + userLogin.getIsLocked());
        final String password = passwEncrypt.encryptPass(loginDto.getPassword());
//        final boolean userPassword = !user1.getPassword().equals(password);
        final boolean userIsLocked = userLogin != null && userLogin.getIsLocked() != null && userLogin.getIsLocked();
        final boolean userLogin1 = user1 != null || !userIsLocked;
        final boolean failedAttempt= user1 != null && !user1.getPassword().equals(password);
        if (user1 == null) {
            apiResponse.setData("User not exists please Register");
            return apiResponse;
        } else if (failedAttempt) {
            return failureAttempt(loginDto);
        } else if (userLogin1) {
            LOGGER.info("User logged in");
            apiResponse.setData("User logged in");
            username = user1.getUserName();
            userLogin.setIsLocked(Boolean.FALSE);
            userLogin.setUserName(loginDto.getUserName());
            userLogin.onSave();
            userLogin.setLoginStatus(Boolean.TRUE);
            userLogin.setFailureAttempts(0);
            loginRepository.save(userLogin);
            apiResponse.setStatus(200);
        }
        if (userLogin.getIsLocked()) {
            timeCheck();
        }
        return apiResponse;
    }

    public APIResponse failureAttempt(LoginDto loginDto) {
        final APIResponse apiResponse1 = new APIResponse();
        if (userLogin.getFailureAttempts() < maxAttempts) {
            int attempts = userLogin.getFailureAttempts();
            userLogin.setFailureAttempts(++attempts);
            userLogin.setIsLocked(Boolean.FALSE);
        }
        if (userLogin.getLockTime() == null && userLogin.getFailureAttempts() == 4) {
            userLogin.setIsLocked(Boolean.TRUE);
            userLogin.setLockTime(new Date());
            LOGGER.info("User Locked wait for 5 minutes");
            apiResponse1.setData("User Locked wait for 5 minutes");
            return apiResponse1;
        }
        apiResponse1.setData("User login failed");
        LOGGER.info("User login failed");
        userLogin.setUserName(loginDto.getUserName());
        userLogin.setLoginStatus(Boolean.FALSE);
        userLogin.onSave();
        loginRepository.save(userLogin);
        return apiResponse1;
    }

    public void timeCheck() {
        final long lockTimeInMillis = userLogin.getLockTime().getTime();
        final long currentTimeInMillis = System.currentTimeMillis();
        if (((lockTimeInMillis / lockTime) * lockTime + lockTime) < currentTimeInMillis) {
            userLogin.setFailureAttempts(0);
            userLogin.setIsLocked(Boolean.FALSE);
            userLogin.setLockTime(null);
        }
    }
}
