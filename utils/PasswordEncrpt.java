package com.lexisnexis.tms.utils;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
@Component
public class PasswordEncrpt {


    public String encryptPass(String encryptPass) throws
            NoSuchAlgorithmException {
        MessageDigest instance =
                MessageDigest.getInstance("MD5");
        instance.update(encryptPass.getBytes());
        byte[] digest = instance.digest();
        StringBuilder builder = new
                StringBuilder();

        for (byte b : digest) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();

    }
}