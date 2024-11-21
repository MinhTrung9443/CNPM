package com.cnpm.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.cnpm.entity.Account;
import com.cnpm.repository.AccountRepository;
import com.cnpm.service.IAccountService;


@Service
public class AccountService implements IAccountService{
	@Autowired
	AccountRepository accRepo;

	@Override
	public <S extends Account> S save(S entity) {
		return accRepo.save(entity);
	}

	@Override
	public <S extends Account> Optional<S> findOne(Example<S> example) {
		return accRepo.findOne(example);
	}
	
	
}
