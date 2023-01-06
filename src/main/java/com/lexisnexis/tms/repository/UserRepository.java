package com.lexisnexis.tms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lexisnexis.tms.entity.User;

public interface UserRepository extends JpaRepository<User, String> {

}
