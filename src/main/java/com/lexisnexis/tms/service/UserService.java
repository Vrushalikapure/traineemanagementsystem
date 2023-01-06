package com.lexisnexis.tms.service;

import java.security.NoSuchAlgorithmException;

import com.lexisnexis.tms.entity.User;
import com.lexisnexis.tms.entity.WorkHistory;
import com.lexisnexis.tms.exception.UserNotFoundException;

public interface UserService {

    //String getDataByUserName(String userName);

    String registerNewUser(User user) throws NoSuchAlgorithmException;

    WorkHistory updateWorkHistory(WorkHistory workHistory);

    User getUserDetails(String userName) throws UserNotFoundException;
}
