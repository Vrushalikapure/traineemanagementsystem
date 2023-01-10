package com.lexisnexis.tms.repository;

import com.lexisnexis.tms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, String>
{
    Boolean existsByUserName(String userName);
    User findByUserName(String userName);
    //public User findByUserName(String username);
    public User findByPassword(String password);

    void deleteByUserName(String userName);
    //void deleteById(String userName);
}
