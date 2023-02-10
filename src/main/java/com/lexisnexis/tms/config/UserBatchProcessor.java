package com.lexisnexis.tms.config;


import com.lexisnexis.tms.entity.UserEntity;
import com.lexisnexis.tms.util.PasswEncrypt;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class UserBatchProcessor implements ItemProcessor<UserEntity, UserEntity> {
    @Autowired
    PasswEncrypt passwEncrypt;

    @Override
    public UserEntity process(UserEntity userEntity) throws Exception {
        userEntity.setPassword(passwEncrypt.encryptPass(userEntity.getPassword()));
        return userEntity;
    }
}