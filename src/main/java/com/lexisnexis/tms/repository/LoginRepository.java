package com.lexisnexis.tms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lexisnexis.tms.entity.UserLogin;

public interface LoginRepository extends JpaRepository<UserLogin, String>
{
    UserLogin findByUserName(String userName);
}
