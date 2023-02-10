package com.lexisnexis.tms.util;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class PasswEncrypt {

    public String encryptPass(String encrptPass) throws
            NoSuchAlgorithmException {
        final MessageDigest instance = MessageDigest.getInstance("MD5");
        instance.update(encrptPass.getBytes());
        final byte[] digest = instance.digest();
        final StringBuilder builder = new StringBuilder();
        for (final byte b : digest) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
