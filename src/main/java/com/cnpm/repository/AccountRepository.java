package com.cnpm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cnpm.entity.Account;



@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{
	Account findByUsernameAndPassword(String username, String password);
	Account findByUsername(String username);
}
