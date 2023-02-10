package com.lexisnexis.tms.repository;

import com.lexisnexis.tms.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<UserEntity, String> {
    Boolean existsByUserName(String userName);

    UserEntity findByUserName(String userName);

    public UserEntity findByPassword(String password);
}
