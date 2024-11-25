package com.cnpm.service;

import java.util.Optional;

import java.util.List;
import org.springframework.stereotype.Service;

import com.cnpm.dto.OrderDetailEmployeeDTO;
import com.cnpm.entity.Order;
import com.cnpm.enums.OrderStatus;


import java.time.LocalDateTime;

@Service
public interface IOrderService{

	void updateOrderStatus(Long orderId, String status);

	void saveOrder(Order user);

	Order getOrderById(Long id);

	List<Order> getAllOrders();

	List<Order> getOrdersByStatus(OrderStatus pending);
	void deleteOrder(Long id);

	OrderDetailEmployeeDTO getOrderDetails(Long orderId);
	boolean processRefund(Long orderId);

	Optional<Order> findById(Long id);

	<S extends Order> S save(S entity);
	void updateOrderStatus(Long orderId, String paymentTime);

    void updateOrderStatus(Long orderId);

    Double getOrderTotal(Long orderId);
}
