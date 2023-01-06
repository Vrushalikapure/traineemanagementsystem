package com.lexisnexis.tms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lexisnexis.tms.entity.UserLogin;
@Repository
public interface LoginRepository extends JpaRepository<UserLogin, String> {

}
