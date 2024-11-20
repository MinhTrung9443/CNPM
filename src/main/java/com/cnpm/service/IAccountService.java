package com.cnpm.service;

import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.cnpm.entity.Account;



@Service
public interface IAccountService {
	<S extends Account> Optional<S> findOne(Example<S> example);

	<S extends Account> S save(S entity);
}
