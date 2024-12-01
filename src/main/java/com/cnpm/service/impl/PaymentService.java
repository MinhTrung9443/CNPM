package com.cnpm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnpm.entity.Payment;
import com.cnpm.repository.PaymentRepository;
import com.cnpm.service.interfaces.IPaymetService;

@Service
public class PaymentService implements IPaymetService{
	@Autowired
	PaymentRepository paymentRepo;

	@Override
	public Payment findByOrder_OrderId(Long orderId) {
		return paymentRepo.findByOrder_OrderId(orderId);
	}
	
	
}	
