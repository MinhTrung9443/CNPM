package com.cnpm.service;

import com.cnpm.dto.OrderDetailEmployeeDTO;
import com.cnpm.dto.PurchaseHistoryDTO;
import com.cnpm.entity.Order;
import com.cnpm.enums.OrderStatus;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public interface IOrderService{
    public Set<PurchaseHistoryDTO> getAllOrders(Long customerId);

    public Set<PurchaseHistoryDTO> getPurchaseHistory(Long customerId, OrderStatus status);

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
	void updateOrderStatusPaymentTime(Long orderId, String paymentTime);

    void updateOrderStatus(Long orderId);

    Double getOrderTotal(Long orderId);
}
