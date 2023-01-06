package com.lexisnexis.tms.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.lexisnexis.tms.entity.User;

@Repository
public interface EmpRepo extends CrudRepository<User, String> {
	
	public User findByUserName(String username);

	public User findByPassword(String password);
	
	Boolean existsByUserName(String userName);
   
	
	
}
