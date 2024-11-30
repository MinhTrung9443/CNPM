package com.cnpm.service.impl;

import com.cnpm.entity.Customer;
import com.cnpm.repository.CustomerRepository;
import com.cnpm.service.interfaces.ICustomerSerivce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService implements ICustomerSerivce {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Customer findByUserId(Long userId) {
        return customerRepository.findByUserId(userId);
    }

}
