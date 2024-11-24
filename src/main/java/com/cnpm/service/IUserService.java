package com.cnpm.service;

import org.springframework.stereotype.Service;

import com.cnpm.entity.Account;
import com.cnpm.entity.User;


@Service
public interface IUserService {
	<S extends User> S save(S entity);
	
	

	User findByEmail(String email);

	User findByPhone(String phone);

	Account findByUsernameAndPassword(String username, String password);

	Account findByUsername(String username);

	void delete(User entity);

}