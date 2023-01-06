package com.lexisnexis.tms.service;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import com.lexisnexis.tms.encrypt.PasswEncrypt;
import com.lexisnexis.tms.entity.User;
import com.lexisnexis.tms.entity.UserLogin;
import com.lexisnexis.tms.exception.UserAlreadyHasAccount;
import com.lexisnexis.tms.exception.UserNotFoundException;
import com.lexisnexis.tms.repository.EmpRepo;
import com.lexisnexis.tms.repository.LoginRepo;

@Service
public class EmpServiceImpl implements EmpService {


	@Autowired
	UserLogin logintable;

	@Autowired
	LoginRepo  loginrepo;

	@Autowired 
	EmpRepo emprepo;

	@Autowired
	PasswEncrypt PasswEncrypt;

	@Override
	public List<User> fetchAllEmpDetail() throws UserNotFoundException {
		long count=emprepo.count();		
		if(count!=0)
		{
			return  (List<User>)emprepo.findAll();
		}
		else
		{
			throw new UserNotFoundException("We Dont Have Any employee yet");
		}
	}
			
	@Override
	public User getDataByUsername(String username) throws UserNotFoundException {

		User emp=emprepo.findByUserName(username); 

		if(emp!=null)
		{
			return emp;
		}
		else
		{
			throw new UserNotFoundException("Usrname name does not Exist"+" "+username);
		}
	}

	@Override
	public void deleteDataByUsername(String username) throws UserNotFoundException {

		User emp=emprepo.findByUserName(username); 

		UserLogin login=loginrepo.findByUserName(username);

		if(emp!=null)
		{
			if(login!=null && login.getLoginStatus().equals("Y")) {
				emprepo.deleteById(username);
				loginrepo.deleteById(username);
			} 
			else
			{
				throw new UserNotFoundException("Else user Has not loginIn"+" "+username);
			}	
		}
		else
		{
			throw new UserNotFoundException("user has not register");
		}

	}

	@Override
	public User addUser(User emp) throws UserAlreadyHasAccount, NoSuchAlgorithmException {

		User emp1=emprepo.findByUserName(emp.getUserName());
		if(emp1!=null) {
			throw new UserAlreadyHasAccount("Employee Already have Account with This Username"+" "+emp1.getUserName());
		}else {
			emp.setPassword(PasswEncrypt.encryptPass(emp.getPassword()));
			return emprepo.save(emp);
		}
	}

	@Override
	public User updateUser(@PathVariable String username,@RequestBody User em) throws UserNotFoundException, NoSuchAlgorithmException{
		User emp=emprepo.findByUserName(username);

		if(emp!=null) {
			em.setPassword(PasswEncrypt.encryptPass(em.getPassword()));
			return	emprepo.save(em);
		}
		else
		{
			throw new UserNotFoundException("Usrname name does not Exist"+" "+username+" "+"So yoou can't Update.");
		}	
	}

	@Override
	public UserLogin loginUser(@RequestBody User emp,
			@RequestBody UserLogin userlogin) throws UserNotFoundException {

		User user=emprepo.findByUserName(userlogin.getUserName());	



		userlogin.setUserName(emp.getUserName());
		return loginrepo.save(userlogin);
	}
}
