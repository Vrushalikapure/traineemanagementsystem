package com.lexisnexis.tms.services;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import com.lexisnexis.tms.exception.UserNotLoginExceptions;
import org.springframework.web.bind.annotation.RequestBody;
import com.lexisnexis.tms.dto.ChangePassword;
import com.lexisnexis.tms.entity.User;
import com.lexisnexis.tms.entity.UserLogin;
import com.lexisnexis.tms.entity.WorkHistory;
import com.lexisnexis.tms.exception.UserNotFoundException;
import com.lexisnexis.tms.exception.UserNotLoginException;
import com.lexisnexis.tms.exception.UserPasswordDoesNotMatching;

public interface UserService {

    public String registerNewUser(User user) throws NoSuchAlgorithmException;

    public List<User> fetchAllUserDetail() throws UserNotFoundException;

    public User getDataByUserName(String userName) throws UserNotFoundException;

    public void deleteDataByUserName(String userName) throws UserNotFoundException;

//    public UserLogin loginUser(@RequestBody User user,@RequestBody UserLogin userlogin)
//            throws UserNotFoundException;

    public String updateUser(User user);

    public String forgotPassword(User user) throws NoSuchAlgorithmException;

    public String changePassword(String userName, ChangePassword changePassword)
            throws UserNotLoginException, NoSuchAlgorithmException, UserPasswordDoesNotMatching, UserNotFoundException,  UserNotLoginExceptions;

    WorkHistory updateWorkHistory(WorkHistory workHistory )throws UserNotLoginException, UserNotFoundException;
}
