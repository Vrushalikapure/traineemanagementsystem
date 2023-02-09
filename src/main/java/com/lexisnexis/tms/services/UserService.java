package com.lexisnexis.tms.services;

import com.lexisnexis.tms.dto.ChangePassword;
import com.lexisnexis.tms.entity.UserEntity;
import com.lexisnexis.tms.entity.WorkHistory;
import com.lexisnexis.tms.exception.UserNotFoundException;
import com.lexisnexis.tms.exception.UserNotLoginException;
import com.lexisnexis.tms.exception.UserNotLoginExceptions;
import com.lexisnexis.tms.exception.UserPasswordDoesNotMatching;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface UserService {

    public String registerNewUser(UserEntity user) throws NoSuchAlgorithmException;

    public List<UserEntity> fetchAllUserDetail() throws UserNotFoundException;

    public UserEntity getDataByUserName(String userName) throws UserNotFoundException;

    public void deleteDataByUserName(String userName) throws UserNotFoundException;

    public String updateUser(UserEntity user);

    public String forgotPassword(UserEntity user) throws NoSuchAlgorithmException;

    public String changePassword(String userName, ChangePassword changePassword)
            throws UserNotLoginException, NoSuchAlgorithmException, UserPasswordDoesNotMatching, UserNotFoundException, UserNotLoginExceptions;

    WorkHistory updateWorkHistory(WorkHistory workHistory) throws UserNotLoginException, UserNotFoundException;

    public CompletableFuture<UserEntity> getUserByUserName(String userName) throws UserNotFoundException;
}
