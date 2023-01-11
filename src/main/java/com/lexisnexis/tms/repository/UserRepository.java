package com.lexisnexis.tms.repository;

import com.lexisnexis.tms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, String>
{
    Boolean existsByUserName(String userName);
    User findByUserName(String userName);
    public User findByPassword(String password);
}
