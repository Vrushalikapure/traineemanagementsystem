package com.lexisnexis.tms.service;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lexisnexis.tms.entity.User;
import com.lexisnexis.tms.repository.UserRepository;
import com.lexisnexis.tms.utils.PasswordEncrpt;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository repository;

	@Autowired
	private PasswordEncrpt passwordEncrpt;

	@Override
	public User saveUser(User user) throws NoSuchAlgorithmException {
		String encryptPass = passwordEncrpt.encryptPass(user.getPassword());
		user.setPassword(encryptPass);
		System.out.print(encryptPass);
		return repository.save(user);
	}

	@Override
	public void updateUser(User user, String userName) {
		Optional<User> findedUser = repository.findById(userName);
		User newUser = findedUser.get();
		if (userName.equals(newUser.getUserName())) {
			newUser.setUserName(userName);
			newUser.setFirstName(user.getFirstName());
			newUser.setLastName(user.getLastName());
			newUser.setEmail(user.getEmail());
			newUser.setLocation(user.getLocation());
			newUser.setMobileNo(user.getMobileNo());
			repository.save(newUser);
		}
		
	}

}