package com.cnpm.service.impl;




import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.cnpm.entity.Account;
import com.cnpm.entity.User;
import com.cnpm.repository.AccountRepository;
import com.cnpm.repository.UserRepository;
import com.cnpm.service.IUserService;



@Service
public class UserService implements IUserService{
	@Autowired
	UserRepository userrepo;
	@Autowired 
	AccountRepository accrepo;
	@Override
	public <S extends User> S save(S entity) {
		return userrepo.save(entity);
	}

	@Override
	public User findByPhone(String phone) {
		return userrepo.findByPhone(phone);
	}

	@Override
	public User findByEmail(String email) {
		return userrepo.findByEmail(email);
	}

	@Override
	public Account findByUsernameAndPassword(String username, String password) {
		return accrepo.findByUsernameAndPassword(username, password);
	}

	@Override
	public Account findByUsername(String username) {
		return accrepo.findByUsername(username);
	}

	@Override
	public void delete(User entity) {
		userrepo.delete(entity);
	}

	@Override
	public Optional<User> findById(Long id) {
		return userrepo.findById(id);
	}
	
	
}
