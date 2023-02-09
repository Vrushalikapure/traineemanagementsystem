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
import com.lexisnexis.tms.util.PasswEncrypt;
import org.junit.Before;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = LoginServiceImplTest.class)
class LoginServiceImplTest {

    @InjectMocks
    LoginServiceImpl loginServiceimpl;

    @Mock
    UserRepository userRepository;
    @Mock
    UserLogin userLogin;
    @Mock
    LoginRepository loginRepository;
    @Mock
    PasswEncrypt passwEncrypt;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void login() throws NoSuchAlgorithmException {
        UserEntity userEntity=new UserEntity();
        userEntity.setUserName("ravikant");
        userEntity.setFirstName("Ravikant");
        userEntity.setLastName("Madas");
        userEntity.setEmail("ravi@gmail.com");
        userEntity.setMobileNo("9175950073");
        userEntity.setPassword("Ravi@123");
        userEntity.setLocation("Solapur");
        LoginDto loginDto=new LoginDto();
        loginDto.setUserName("ravikant");
        loginDto.setPassword("ravi@123");
        Mockito.when(userRepository.findByUserName("ravikant")).thenReturn(userEntity);
        Mockito.when(userLogin.getIsLocked()).thenReturn(false);
        Mockito.when(passwEncrypt.encryptPass(loginDto.getPassword())).thenReturn("e58cc3fe4b3387c893c8fc9dd43a829a");
        assertEquals(200,loginServiceimpl.login(loginDto).getStatus());
    }
    @Test
    void loginUseerNotExists() throws NoSuchAlgorithmException {
        UserEntity userEntity= new UserEntity();
        LoginDto loginDto=new LoginDto();
        loginDto.setUserName("ravikant");
        loginDto.setPassword("ravi@123");
        Mockito.when(userRepository.findByUserName("ravikant")).thenReturn(userEntity);
        Mockito.when(userLogin.getIsLocked()).thenReturn(false);
        Mockito.when(passwEncrypt.encryptPass(loginDto.getPassword())).thenReturn("e58cc3fe4b3387c893c8fc9dd43a829a");
        assertEquals(200,loginServiceimpl.login(loginDto).getStatus());
    }

    @Test
    void failureAttempt() {
    }

    @Test
    void timeCheck() {

    }
}