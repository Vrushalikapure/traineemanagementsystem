package com.lexisnexis.tms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lexisnexis.tms.entity.UserLogin;

public interface LoginTableRepository extends JpaRepository<UserLogin, Integer>
{
   // List<LoginTable> findByuserName(String userName);
    UserLogin findByUserName(String userName);
}
