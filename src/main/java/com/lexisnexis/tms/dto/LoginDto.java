package com.lexisnexis.tms.dto;

import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

@Component
public class LoginDto {

    @NotBlank(message = "userName cannot be empty!")
    private String userName;
    @NotBlank(message = "password cannot be empty!")
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
