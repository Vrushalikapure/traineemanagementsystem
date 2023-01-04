package com.lexisnexis.tms.util;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
@Component
public class PasswEncrypt {

    public  String encryptPass(String encrptPass) throws
            NoSuchAlgorithmException { MessageDigest instance =
            MessageDigest.getInstance("MD5"); instance.update(encrptPass.getBytes());
        byte[] digest = instance.digest(); StringBuilder builder = new
                StringBuilder();

        for(byte b:digest) { builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
