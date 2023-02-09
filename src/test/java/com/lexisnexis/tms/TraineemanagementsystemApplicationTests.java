package com.lexisnexis.tms;

import com.lexisnexis.tms.dto.LoginDto;
import com.lexisnexis.tms.entity.UserEntity;
import com.lexisnexis.tms.repository.UserRepository;
import com.lexisnexis.tms.serviceimpl.LoginServiceImpl;
import com.lexisnexis.tms.serviceimpl.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@SpringBootTest
class TraineemanagementsystemApplicationTests {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private LoginServiceImpl loginServiceimpl;

    @Autowired
    private UserRepository userRepository;

    @Test
    void getAllUser() {
        List<UserEntity> allUser = userServiceImpl.fetchAllUserDetail();
        Assertions.assertThat(allUser).isNotNull();
    }

    @Test
    void getUserByUsername() {
        UserEntity dataByUserName = userServiceImpl.getDataByUserName("ravikant");
        Assertions.assertThat(dataByUserName.getUserName()).isEqualTo("ravikant");
    }

    @Test
    void login() throws NoSuchAlgorithmException, InterruptedException {
        LoginDto loginDto=new LoginDto();
        loginDto.setUserName("ravikant");
        loginDto.setPassword("ravi@123");
        org.junit.jupiter.api.Assertions.assertEquals(200,loginServiceimpl.login(loginDto).getStatus());
    }

    @Test
    void deleteDataByUserName() {
        userServiceImpl.deleteDataByUserName("ravikant");
        UserEntity findByUserName = userRepository.findByUserName("ravikant");
        Assertions.assertThat(findByUserName).isNull();
    }

}
