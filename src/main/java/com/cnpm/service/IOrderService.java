package com.cnpm.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.cnpm.dto.OrderDetailEmployeeDTO;
import com.cnpm.entity.Order;
import com.cnpm.enums.OrderStatus;

@Service
public interface IOrderService{

	void updateOrderStatus(Long orderId, String status);

	void saveOrder(Order user);

	Order getOrderById(Long id);

	List<Order> getAllOrders();

	List<Order> getOrdersByStatus(OrderStatus pending);
	void deleteOrder(Long id);

	OrderDetailEmployeeDTO getOrderDetails(Long orderId);

}
