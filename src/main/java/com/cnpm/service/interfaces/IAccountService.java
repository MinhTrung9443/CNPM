package com.cnpm.service.interfaces;

import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.cnpm.entity.Account;



@Service
public interface IAccountService {
	<S extends Account> Optional<S> findOne(Example<S> example);

	<S extends Account> S save(S entity);

	void deleteByAccountId(Long id);
}
