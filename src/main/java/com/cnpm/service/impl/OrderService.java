package com.cnpm.service.impl;

import com.cnpm.entity.Order;
import com.cnpm.repository.OrderRepository;
import com.cnpm.service.IOrderService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService implements IOrderService {
	@Autowired
	private OrderRepository orderrepo;

	@Override
	public Optional<Order> findById(Long id) {
		return orderrepo.findById(id);
	}

	@Override
	public <S extends Order> S save(S entity) {
		return orderrepo.save(entity);
	}
	
	
	
}
