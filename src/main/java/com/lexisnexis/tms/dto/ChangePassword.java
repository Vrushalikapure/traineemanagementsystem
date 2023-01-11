package com.lexisnexis.tms.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

//@Component
public class ChangePassword {

    @NotBlank(message =  "oldPassword cannot be empty!")
    @NotNull(message = "oldPassword cannot be empty!")
    private String oldPassword;
    @NotBlank(message = "newPassword cannot be empty!")
    @NotNull(message = "newPassword cannot be empty!")
    private String newPassword;


    public ChangePassword(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
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
