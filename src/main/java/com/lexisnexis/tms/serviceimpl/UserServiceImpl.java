package com.lexisnexis.tms.serviceimpl;

import com.lexisnexis.tms.dto.ChangePassword;
import com.lexisnexis.tms.entity.UserEntity;
import com.lexisnexis.tms.entity.UserLogin;
import com.lexisnexis.tms.entity.WorkHistory;
import com.lexisnexis.tms.exception.UserNotFoundException;
import com.lexisnexis.tms.exception.UserNotLoginException;
import com.lexisnexis.tms.exception.UserNotLoginExceptions;
import com.lexisnexis.tms.exception.UserPasswordDoesNotMatching;
import com.lexisnexis.tms.exception.UserNamedoesNotMatchException;
import com.lexisnexis.tms.exception.UserNameAlreadyExistException;
import com.lexisnexis.tms.repository.LoginRepository;
import com.lexisnexis.tms.repository.UserRepository;
import com.lexisnexis.tms.repository.WorkHistoryRepository;
import com.lexisnexis.tms.services.UserService;
import com.lexisnexis.tms.util.PasswEncrypt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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

    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository userRepository, LoginRepository loginRepository, WorkHistoryRepository workHistoryRepository, PasswEncrypt passwEncrypt) {
        this.userRepository = userRepository;
        this.loginRepository = loginRepository;
        this.workHistoryRepository = workHistoryRepository;
        this.passwEncrypt = passwEncrypt;
    }

    @Override
    public String registerNewUser(UserEntity user) {
        // add check for username exists in database
        if (userRepository.existsByUserName(user.getUserName())) {
            throw new UserNameAlreadyExistException();
        } else {
            userRepository.save(user);
        }
        return "UserEntity registered successfully!.";
    }

    @Override
    public WorkHistory updateWorkHistory(@RequestBody WorkHistory workHistory)
            throws UserNotLoginException, UserNotFoundException {
        final UserLogin userLogin = loginRepository.findByUserName(workHistory.getUserName());
        final boolean user1 = userLogin != null;
        if (user1) {
            if ("false".equals(userLogin.getLoginStatus())) {
                throw new UserNotLoginException("Please Login!!!");
            } else {
                final LocalDateTime loginTime = userLogin.getLoginTime();
                workHistory.setLoginAt(loginTime);
                return workHistoryRepository.save(workHistory);
            }
        } else {
            throw new UserNotFoundException("user not found for userName: " + workHistory.getUserName());
        }
    }

    @Async
    @Override
    public CompletableFuture<UserEntity> getUserByUserName(String userName) throws UserNotFoundException {
        return CompletableFuture.completedFuture(getDataByUserName(userName));
    }

    @Override
    public List<UserEntity> fetchAllUserDetail() throws UserNotFoundException {
        final long count = userRepository.count();
        final boolean count1 = count != 0;
        if (count1) {
            return (List<UserEntity>) userRepository.findAll();
        } else {
            throw new UserNotFoundException("We Don't Have Any user yet");
        }
    }

    @Override
    public UserEntity getDataByUserName(String userName) throws UserNotFoundException {
        final UserEntity user = userRepository.findByUserName(userName);
        final boolean user1 = user != null;
        if (user1) {
            return user;
        } else {
            throw new UserNotFoundException("UserName does not Exist" + " " + userName);
        }
    }

    @Async
    @Override
    public void deleteDataByUserName(String userName) throws UserNotFoundException {
        final UserEntity user = userRepository.findByUserName(userName);
        final UserLogin userLogin = loginRepository.findByUserName(userName);
        final boolean userLogin2 = userLogin != null;
        final boolean userLoginStatus = userLogin.getLoginStatus();
        final boolean user1 = user != null;
        if (user1) {
            if (userLogin2 && userLoginStatus) {
                userRepository.deleteById(userName);
                loginRepository.deleteById(userName);
            } else {
                throw new UserNotFoundException("Else user Has not loginIn" + " " + userName);
            }
        } else {
            throw new UserNotFoundException("user has not register");
        }
        LOGGER.info("UserEntity Deleted" + Thread.currentThread().getName());
    }

    @Override
    public String updateUser(UserEntity user) throws UserNotFoundException {
        final Boolean existsByUserName = userRepository.existsByUserName(user.getUserName());
        if (existsByUserName) {
            final UserEntity newUser = new UserEntity();
            newUser.setFirstName(user.getFirstName());
            newUser.setLastName(user.getLastName());
            newUser.setEmail(user.getEmail());
            newUser.setLocation(user.getLocation());
            newUser.setMobileNo(user.getMobileNo());
            userRepository.save(newUser);
            LOGGER.debug("UserEntity data updated successfully!");
            return "UserEntity Data updated successfully";
        } else {
            throw new UserNotFoundException("UserEntity Not Found");
        }
    }

    @Override
    public String forgotPassword(UserEntity user) throws NoSuchAlgorithmException {
        final UserEntity existingUser = userRepository.findByUserName(user.getUserName());
        if (existingUser == null) {
            throw new UserNamedoesNotMatchException();
        } else {
            existingUser.setUserName(user.getUserName());
            existingUser.setPassword(passwEncrypt.encryptPass(user.getPassword()));
            userRepository.save(existingUser);
            LOGGER.debug("Password updated successfully!");
            return "Password has been update successfully";
        }
    }

    @Override
    public String changePassword(String userName, ChangePassword changePassword)
            throws UserNotLoginException, NoSuchAlgorithmException, UserPasswordDoesNotMatching,
            UserNotLoginExceptions {
        final UserLogin findByUserName = loginRepository.findByUserName(userName);
        final boolean userFound = loginRepository.findByUserName(userName) != null;
        if (userFound) {
            final UserEntity user = userRepository.findByUserName(userName);
            final String dbPass = user.getPassword();
            final String oldPass = changePassword.getOldPassword();
            if (dbPass.equals(oldPass) && findByUserName.getLoginStatus()) {
                user.setPassword(changePassword.getNewPassword());
            } else {
                throw new UserNotLoginException("UserEntity Not login this time");
            }
        } else {
            throw new UserNotLoginExceptions("UserEntity NotFound");
        }
        return "Password Changed successfully";
    }
//    @Override
//    public String changePassword(String userName, ChangePassword changePassword)
//            throws UserNotLoginException, NoSuchAlgorithmException, UserPasswordDoesNotMatching, UserNotLoginExceptions {
//
//        final Optional<UserLogin> findById = loginRepository.findById(userName);
//        if (findById.isPresent()) {
//            final UserEntity loginUser = userRepository.findById(userName).get();
//            final String dbPass = loginUser.getPassword();
//            final String userName2 = findById.get().getUserName();
//            final boolean userName3 = userName2 != null;
//            final Boolean isLogin = findById.get().getLoginStatus();
//            final boolean isLogin1 = "true".equals(isLogin);
//            final String oldPass = passwEncrypt.encryptPass(changePassword.getOldPassword());
//            if (userName3 && isLogin1) {
//                if (oldPass.equals(dbPass)) {
//                    loginUser.setPassword(passwEncrypt.encryptPass(changePassword.getNewPassword()));
//                    userRepository.save(loginUser);
//                    LOGGER.debug("change password() completed ");
//                } else {
//
//                    LOGGER.debug("change password username not found in db");
//                    throw new UserPasswordDoesNotMatching("UserEntity password does not matching");
//                }
//            } else {
//                throw new UserNotLoginException("UserEntity Not login this time");
//            }
//        } else {
//            throw new UserNotLoginExceptions("UserEntity Not login this time");
//        }
//        return "Password Changed successfully";
//    }
}
