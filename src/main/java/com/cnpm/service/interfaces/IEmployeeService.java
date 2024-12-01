package com.cnpm.service.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cnpm.entity.Account;
import com.cnpm.entity.Employee;

public interface IEmployeeService {

	void deleteById(Long id);

	long count();

	Optional<Employee> findById(Long id);

	<S extends Employee> S save(S entity);

	List<Employee> findAll();

	Page<Employee> findAll(Pageable pageable);

	Page<Employee> findByFullNameContaining(String fullName, Pageable pageable);

	Employee findByPhone(String phone);

	Employee findByEmail(String email);

	Account findByUsername(String username);

	void deleteByAccountId(Long accountId);	
}
