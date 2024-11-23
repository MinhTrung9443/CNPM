package com.cnpm.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cnpm.entity.Account;
import com.cnpm.entity.Employee;
import com.cnpm.repository.AccountRepository;
import com.cnpm.repository.EmployeeRepository;
import com.cnpm.service.IEmployeeService;

@Service
public class EmployeeService implements IEmployeeService{
	@Autowired
	EmployeeRepository eRepository;
	
	@Autowired
	AccountRepository accRepository;

	public EmployeeService(EmployeeRepository eRepository) {
		this.eRepository = eRepository;
	}

	@Override
	public <S extends Employee> S save(S entity) {
		return eRepository.save(entity);
	}

	@Override
	public Optional<Employee> findById(Long id) {
		return eRepository.findById(id);
	}

	@Override
	public long count() {
		return eRepository.count();
	}

	@Override
	public void deleteById(Long id) {
		eRepository.deleteById(id);
	}

	@Override
	public List<Employee> findAll() {
		return eRepository.findAll();
	}

	@Override
	public Page<Employee> findAll(Pageable pageable) {
		return eRepository.findAll(pageable);
	}

	@Override
	public Page<Employee> findByFullNameContaining(String fullName, Pageable pageable) {
		return eRepository.findByFullNameContaining(fullName, pageable);
	}
	
	@Override
	public Employee findByEmail(String email) {
		return eRepository.findByEmail(email);
	}
	
	@Override
	public Employee findByPhone(String phone) {
		return eRepository.findByPhone(phone);
	}

	@Override
	public Account findByUsername(String username) {
		return accRepository.findByUsername(username);
	}
	
	@Override
	public void deleteByAccountId(Long accountId) {
		accRepository.deleteByAccountId(accountId);
	}
	
}
