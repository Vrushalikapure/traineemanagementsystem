package com.lexisnexis.tms.services;

import com.lexisnexis.tms.dto.LoginDto;
import com.lexisnexis.tms.response.APIResponse;

import java.security.NoSuchAlgorithmException;

public interface LoginService {
    public APIResponse login(LoginDto loginDto) throws InterruptedException, NoSuchAlgorithmException;
}
