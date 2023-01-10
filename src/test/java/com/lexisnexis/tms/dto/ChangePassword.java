package com.lexisnexis.tms.dto;

import lombok.Data;

@Data
public class ChangePassword {
	private String oldPassword;
	private String newPassword;

}
