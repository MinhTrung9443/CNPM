package com.cnpm.service;

import org.springframework.stereotype.Service;

import com.cnpm.entity.Customer;

@Service
public interface ICustomerSerivce {
	 Customer findByUserId(Long userId);
}
