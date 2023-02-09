package com.lexisnexis.tms.repository;

import com.lexisnexis.tms.entity.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<UserLogin, String> {
    UserLogin findByUserName(String userName);
}
