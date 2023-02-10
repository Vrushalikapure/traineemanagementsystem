//package com.lexisnexis.tms.serviceimpl;
//
//import com.lexisnexis.tms.entity.UserLogin;
//import com.lexisnexis.tms.repository.LoginRepository;
//import com.lexisnexis.tms.repository.UserRepository;
//import com.lexisnexis.tms.util.PasswEncrypt;
//import org.junit.Before;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.powermock.api.mockito.PowerMockito;
//
//class LoginServiceImplTest {
//    UserRepository mockUerRepository;
//    UserLogin mockUserLogin;
//    LoginRepository mockLoginRepository;
//    PasswEncrypt mockPasswEncrypt;
//
//    @Before
//    public void init() {
//        mockUerRepository = PowerMockito.mock(UserRepository.class);
//        mockUserLogin= PowerMockito.mock(UserLogin.class);
//        mockLoginRepository = PowerMockito.mock(LoginRepository.class);
//        mockPasswEncrypt = PowerMockito.mock(PasswEncrypt.class);
//
//    }
//
//
//    @Test
//    void login() {
//    }
//
//    @Test
//    void failureAttempt() {
//    }
//
//    @Test
//    @Order(1)
//    void timeCheck() {
//        LoginServiceImpl mockLoginServiceimpl =new LoginServiceImpl();
//        LoginServiceImpl spyMockLoginServiceimpl = PowerMockito.spy(mockLoginServiceimpl);
//        PowerMockito.doNothing().when(spyMockLoginServiceimpl);
//        spyMockLoginServiceimpl.timeCheck();
//    }
//}

package com.lexisnexis.tms.serviceimpl;

import com.lexisnexis.tms.dto.LoginDto;
import com.lexisnexis.tms.entity.UserEntity;
import com.lexisnexis.tms.entity.UserLogin;
import com.lexisnexis.tms.repository.LoginRepository;
import com.lexisnexis.tms.repository.UserRepository;
import com.lexisnexis.tms.response.APIResponse;
import com.lexisnexis.tms.util.PasswEncrypt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import java.security.NoSuchAlgorithmException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginServiceImplTest {
    UserRepository userRepository;
    LoginRepository loginRepository;
    LoginServiceImpl loginServiceImpl;
    PasswEncrypt passwEncrypt;
    APIResponse apiResponse;
    UserLogin userLogin;
    LoginDto loginDto;

    @BeforeEach
    private void initializeLoginServiceImpl() {
        userRepository = mock(UserRepository.class);
        loginRepository = mock(LoginRepository.class);
        passwEncrypt = mock(PasswEncrypt.class);
        apiResponse = mock(APIResponse.class);
        userLogin = mock(UserLogin.class);
        loginDto = mock(LoginDto.class);
        loginServiceImpl = new LoginServiceImpl(userRepository, loginRepository, passwEncrypt, userLogin);
    }

    @Test
    public void userLoginWithInValidPassword() throws NoSuchAlgorithmException, InterruptedException {
        int maxAttempts = 4;
        LoginDto loginDto = new LoginDto();
        loginDto.setUserName("vrushali");
        loginDto.setPassword("6789");
        UserLogin userLogin = new UserLogin();
        userLogin.setIsLocked(false);
        userLogin.setFailureAttempts(1);
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName("vrushali");
        userEntity.setPassword("1234");
        when(userRepository.findByUserName("vrushali")).thenReturn(userEntity);
        when(loginRepository.findByUserName("vrushali")).thenReturn(userLogin);
        LoginServiceImpl loginServiceImpl = new LoginServiceImpl(userRepository, loginRepository, passwEncrypt, userLogin);
        loginServiceImpl.login(loginDto);
    }

    @Test
    public void userLoginWithInValidPasswordAndFailureAttemptsIs4() throws NoSuchAlgorithmException, InterruptedException {
        int maxAttempts = 4;
        LoginDto loginDto = new LoginDto();
        loginDto.setUserName("vrushali");
        loginDto.setPassword("7876");
        UserLogin userLogin = new UserLogin();
        userLogin.setFailureAttempts(4);
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName("vrushali");
        userEntity.setPassword("1234");
        when(userRepository.findByUserName("vrushali")).thenReturn(userEntity);
        when(loginRepository.findByUserName("vrushali")).thenReturn(userLogin);

        LoginServiceImpl loginServiceImpl = new LoginServiceImpl(userRepository, loginRepository, passwEncrypt, userLogin);
        loginServiceImpl.login(loginDto);
    }

    @Test
    public void userLoginWithValidPassword() throws NoSuchAlgorithmException, InterruptedException {
        LoginDto loginDto = new LoginDto();
        loginDto.setUserName("vrushali");
        loginDto.setPassword("1234");
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName("vrushali");
        userEntity.setPassword("1234");
        UserLogin userLogin = new UserLogin();
        userLogin.setFailureAttempts(1);
        userLogin.setIsLocked(false);
        when(userRepository.findByUserName("vrushali")).thenReturn(userEntity);
        LoginServiceImpl loginServiceImpl = new LoginServiceImpl(userRepository, loginRepository, passwEncrypt, userLogin);
        loginServiceImpl.login(loginDto);
    }

    @Test
    public void userLoginWhenUserIsNull() throws NoSuchAlgorithmException {
        LoginDto loginDto = new LoginDto();
        loginDto.setUserName("abcd");
        loginDto.setPassword("1234");
        UserEntity userEntity = new UserEntity();
        UserRepository userRepository = mock(UserRepository.class);
        userRepository.save(userEntity);
        when(userRepository.findByUserName("abcd")).thenReturn(userEntity);
        loginServiceImpl.login(loginDto);
    }

    @Test
    public void userLoginWhenIsLockedTrueTest() throws NoSuchAlgorithmException, InterruptedException {
        int maxAttempts = 4;
        LoginDto loginDto = new LoginDto();
        loginDto.setUserName("vrushali");
        loginDto.setPassword("7876");
        UserLogin userLogin = new UserLogin();
        userLogin.setIsLocked(true);
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName("vrushali");
        userEntity.setPassword("7876");
        when(userRepository.findByUserName("vrushali")).thenReturn(userEntity);
        when(loginRepository.findByUserName("vrushali")).thenReturn(userLogin);
        LoginServiceImpl loginServiceImpl = new LoginServiceImpl(userRepository, loginRepository, passwEncrypt, userLogin);
        loginServiceImpl.login(loginDto);
    }

    @Test
    public void testtimeCheck() {
        loginServiceImpl=mock(LoginServiceImpl.class);
        Mockito.doNothing().when(loginServiceImpl).timeCheck();
        loginServiceImpl.timeCheck();
    }

}
