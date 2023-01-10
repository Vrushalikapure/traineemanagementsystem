package com.lexisnexis.tms.dto;

import org.springframework.stereotype.Component;

//@Component
public class ChangePassword {

    private String userName;
    private String oldPassword;
    private String newPassword;


    public ChangePassword(String oldPassword, String newPassword, String userName) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
