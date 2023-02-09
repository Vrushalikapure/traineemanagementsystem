package com.lexisnexis.tms.serviceimpl;

import com.lexisnexis.tms.dto.ChangePassword;
import com.lexisnexis.tms.entity.UserEntity;
import com.lexisnexis.tms.entity.UserLogin;
import com.lexisnexis.tms.entity.WorkHistory;
import com.lexisnexis.tms.exception.*;
import com.lexisnexis.tms.repository.LoginRepository;
import com.lexisnexis.tms.repository.UserRepository;
import com.lexisnexis.tms.repository.WorkHistoryRepository;
import com.lexisnexis.tms.response.APIResponse;
import com.lexisnexis.tms.util.PasswEncrypt;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class UserServiceImplTest {

    UserRepository userRepository;

    LoginRepository loginRepository;

    UserServiceImpl userServiceImpl;

    LoginServiceImpl loginServiceImpl;

    WorkHistoryRepository workHistoryRepository;

    PasswEncrypt passwEncrypt;

    UserLogin userLogin;

    UserNotLoginException userNotLoginException;

    Optional<com.lexisnexis.tms.entity.UserEntity> UserEntity;

    APIResponse apiResponse;

    @BeforeAll
    private void initializeUserServiceImpl() {
        userRepository = mock(UserRepository.class);
        loginRepository = mock(LoginRepository.class);
        workHistoryRepository = mock(WorkHistoryRepository.class);
        passwEncrypt = mock(PasswEncrypt.class);
        userServiceImpl = new UserServiceImpl(userRepository, loginRepository, workHistoryRepository, passwEncrypt);
    }

    @Test
    public void getDataByUserNameWhenUserExist() {
        com.lexisnexis.tms.entity.UserEntity user = new UserEntity();
        user.setUserName("neha17");
        when(userRepository.findByUserName("neha17")).thenReturn(user);
        userServiceImpl.getDataByUserName("neha17");
    }

    @Test
    public void getDataByUserNameWhenUserDoesNotExist() {
        com.lexisnexis.tms.entity.UserEntity user = new UserEntity();
        user.setUserName("abcd");
        when(userRepository.findByUserName("vrushali")).thenReturn(null);
        try {
            userServiceImpl.getDataByUserName("vrushali");
        } catch (UserNotFoundException userNotFoundException) {
        }
    }

    @Test
    public void deleteDataByUserNameValidUser() {
        com.lexisnexis.tms.entity.UserEntity user = new UserEntity();
        user.setUserName("sam11");
        UserLogin userLogin = new UserLogin();
        userLogin.setUserName("sam11");
        userLogin.setLoginStatus(true);
        when(userRepository.findByUserName("sam11")).thenReturn(user);
        when(loginRepository.findByUserName("sam11")).thenReturn(userLogin);
        userServiceImpl.deleteDataByUserName("sam11");
        userRepository.deleteById(user.getUserName());
        loginRepository.deleteById(userLogin.getUserName());
    }

    @Test


    public void shouldThrowUserNotFoundException() {
        UserRepository userRepository = mock(UserRepository.class);
        com.lexisnexis.tms.entity.UserEntity user = new UserEntity();
        user.setUserName("shubham");
        when(userRepository.findByUserName("deepak")).thenReturn(null);
        try {
            userServiceImpl.deleteDataByUserName("deepak");
        } catch (Exception e) {
        }
    }

    @Test


    public void deleteDataByUserNameWhileUserIsNotLogin() {
        com.lexisnexis.tms.entity.UserEntity user = new UserEntity();
        user.setUserName("sam11");
        when(userRepository.findByUserName("sam11")).thenReturn(user);
        when(loginRepository.findByUserName("sam11")).thenReturn(null);
        try {
            userServiceImpl.deleteDataByUserName("sam11");
        } catch (Exception e) {
        }
    }

    @Test


    public void updateUserWhenUserExist() {
        com.lexisnexis.tms.entity.UserEntity user = new UserEntity();
        user.setUserName("sam");
        when(userRepository.existsByUserName("sam")).thenReturn(true);
        userServiceImpl.updateUser(user);
    }

    @Test


    public void updateUserWhenUserDoesNotExist() {
        com.lexisnexis.tms.entity.UserEntity user = new UserEntity();
        user.setUserName("sam");
        when(userRepository.existsByUserName("www")).thenReturn(false);
        try {
            userServiceImpl.updateUser(user);
        } catch (UserNotFoundException userNotFoundException) {
        }
    }

    @Test


    public void registerNewUserIfUserNameDoesNotExist() {
        com.lexisnexis.tms.entity.UserEntity userEntity = new UserEntity();
        userEntity.setUserName("vrushali17");
        when(userRepository.existsByUserName("vrushali17")).thenReturn(false);
        userServiceImpl.registerNewUser(userEntity);
    }

    @Test
    public void registerNewUserIfUserNameExist() {
        com.lexisnexis.tms.entity.UserEntity userEntity = new UserEntity();
        userEntity.setUserName("vrushali17");
        when(userRepository.existsByUserName("vrushali17")).thenReturn(true);
        try {
            userServiceImpl.registerNewUser(userEntity);
        } catch (UserNameAlreadyExistException userNameAlreadyExistException) {
        }
    }

    @Test
    public void updateWorkHistoryIfUserIsLoggedInTest() throws UserNotFoundException, UserNotLoginException {
        com.lexisnexis.tms.entity.UserEntity userEntity = new UserEntity();
        userEntity.setUserName("vrushali");
        UserLogin userLogin = new UserLogin();
        userLogin.setUserName("vrushali");
        userLogin.setLoginStatus(true);
        WorkHistory workHistory = new WorkHistory();
        workHistory.setUserName(userLogin.getUserName());
        when(userRepository.findByUserName("vrushali")).thenReturn(userEntity);
        when(loginRepository.findByUserName("vrushali")).thenReturn(userLogin);
        when(workHistoryRepository.findByUserName("vrushali")).thenReturn(workHistory);
        userServiceImpl.updateWorkHistory(workHistory);
    }

    @Test
    public void updateWorkHistoryIfUserIsNotLoggedInTest() {
        com.lexisnexis.tms.entity.UserEntity userEntity = new UserEntity();
        userEntity.setUserName("vrushali");
        UserLogin userLogin = new UserLogin();
        userLogin.setUserName("vrushali");
        userLogin.setLoginStatus(false);
        WorkHistory workHistory = new WorkHistory();
        workHistory.setUserName(userLogin.getUserName());
        when(userRepository.findByUserName("vrushali")).thenReturn(userEntity);
        when(loginRepository.findByUserName("vrushali")).thenReturn(userLogin);
        when(workHistoryRepository.findByUserName("vrushali")).thenReturn(workHistory);
        try {
            userServiceImpl.updateWorkHistory(workHistory);
        } catch (UserNotLoginException userNotLoginException) {
        }
    }

    @Test
    public void updateWorkHistoryWhenUserDoesNotExist() throws UserNotLoginException {
        com.lexisnexis.tms.entity.UserEntity userEntity = new UserEntity();
        userEntity.setUserName("vrushali");
        UserLogin userLogin = new UserLogin();
        userLogin.setUserName("vrushali");
        userLogin.setLoginStatus(false);
        WorkHistory workHistory = new WorkHistory();
        workHistory.setUserName(userLogin.getUserName());
        when(userRepository.findByUserName("vrushali")).thenReturn(userEntity);
        when(loginRepository.findByUserName("vrushali")).thenReturn(null);
        when(workHistoryRepository.findByUserName("vrushali")).thenReturn(workHistory);
        try {
            userServiceImpl.updateWorkHistory(workHistory);
        } catch (UserNotFoundException userNotFoundException) {
        }
    }

    @Test
    public void fetchAllUserDetailsWhenUsersCountIsNotZero() {
        when(userRepository.count()).thenReturn((long) 2);
        userServiceImpl.fetchAllUserDetail();
    }

    @Test
    public void fetchAllUserDetailsWhenUsersCountIsEqualToZero() {
        when(userRepository.count()).thenReturn((long) 0);
        try {
            userServiceImpl.fetchAllUserDetail();
        } catch (UserNotFoundException userNotFoundException) {
        }
    }

    @Test
    public void forgotPasswordInValidUser() throws UserNotFoundException {
        com.lexisnexis.tms.entity.UserEntity user = new UserEntity();
        user.setUserName("sam11");
        when(userRepository.findByUserName("sam")).thenReturn(null);
        try {
            userServiceImpl.forgotPassword(user);
        } catch (UserNotFoundException | NoSuchAlgorithmException userNotFoundException) {
        }
    }

    @Test
    public void forgotPasswordValidUser() throws NoSuchAlgorithmException {
        com.lexisnexis.tms.entity.UserEntity user = new UserEntity();
        user.setUserName("sam11");
        when(userRepository.findByUserName("sam11")).thenReturn(user);
        userServiceImpl.forgotPassword(user);
    }

    @Test
    public void changePasswordWhenUserNotFound() throws NoSuchAlgorithmException,
            UserNotLoginException, UserPasswordDoesNotMatching {
        UserLogin user = new UserLogin();
        ChangePassword changePassword = new ChangePassword();
        when(loginRepository.findByUserName("sam")).thenReturn(null);
        try {
            userServiceImpl.changePassword("sam", changePassword);
        } catch (UserNotLoginExceptions userNotLoginExceptions) {
        }
    }

    @Test
    public void changePasswordWhenUserIsNotLoginIn() throws NoSuchAlgorithmException, UserNotLoginException, UserPasswordDoesNotMatching {
        UserLogin user = new UserLogin();
        user.setUserName("sam");
        user.setLoginStatus(false);
        com.lexisnexis.tms.entity.UserEntity usere = new UserEntity();
        usere.setPassword("sam");
        ChangePassword changePassword = new ChangePassword();
        changePassword.setNewPassword( "samm");
        changePassword.setOldPassword("sam");
        when(loginRepository.findByUserName("sam")).thenReturn(user);
        when(userRepository.findByUserName("sam")).thenReturn(usere);
        try {
            userServiceImpl.changePassword("sam", changePassword);
        } catch (UserNotLoginException userNotLoginExceptions) {
        }
    }

    @Test
    public void changePasswordWhenUserIsLoginIn() throws NoSuchAlgorithmException, UserNotLoginException, UserPasswordDoesNotMatching {
        UserLogin user = new UserLogin();
        user.setUserName("sam");
        user.setLoginStatus(true);
        com.lexisnexis.tms.entity.UserEntity usere = new UserEntity();
        usere.setPassword("sam");
        ChangePassword changePassword = new ChangePassword();
        changePassword.setNewPassword( "samm");
        changePassword.setOldPassword("sam");
        when(loginRepository.findByUserName("sam")).thenReturn(user);
        when(userRepository.findByUserName("sam")).thenReturn(usere);
        userServiceImpl.changePassword("sam", changePassword);
    }

}