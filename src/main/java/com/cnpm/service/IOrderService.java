package com.cnpm.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cnpm.entity.Order;

@Service
public interface IOrderService{

	Optional<Order> findById(Long id);

	<S extends Order> S save(S entity);

}
