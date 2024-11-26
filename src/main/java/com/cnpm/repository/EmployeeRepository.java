package com.cnpm.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cnpm.entity.Account;
import com.cnpm.entity.Employee;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	Optional<Employee> findById(Long Id);
	
	Page<Employee> findAll(Pageable pageable);

	Page<Employee> findByFullNameContaining(String fullName, Pageable pageable);
	
	Employee findByEmail(String email);
	
	Employee findByPhone(String phone);
}
