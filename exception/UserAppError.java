package com.lexisnexis.tms.exception;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class UserAppError {
	private Integer errorCode;
	private String errorDesc;
	private Date date;

}
