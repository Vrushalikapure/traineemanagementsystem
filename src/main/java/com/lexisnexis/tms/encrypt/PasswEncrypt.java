package com.lexisnexis.tms.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;

@Component
public class PasswEncrypt {

	public  String encryptPass(String encrptPass) throws NoSuchAlgorithmException { 
		
	MessageDigest instance = MessageDigest.getInstance("MD5"); 
	instance.update(encrptPass.getBytes());
	byte[] digest = instance.digest();
	StringBuilder builder = new	StringBuilder();

	for(byte b:digest) 
	{ 
		builder.append(String.format("%02x", b));
	}
	return builder.toString();
	}
}