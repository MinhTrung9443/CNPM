package com.cnpm.service.interfaces;

import com.cnpm.dto.OrderDetailEmployeeDTO;
import com.cnpm.dto.PurchaseHistoryDTO;
import com.cnpm.entity.Order;
import com.cnpm.enums.OrderStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public interface IOrderService{
    public Page<PurchaseHistoryDTO> getAllOrders(Long customerId,Pageable pageable);

    public Set<PurchaseHistoryDTO> getPurchaseHistory(Long customerId, OrderStatus status);

	void updateOrderStatus(Long orderId, String status);

	void saveOrder(Order user);

	Order getOrderById(Long id);

	Page<Order> getAllOrders(Pageable pageable);

	  Page<Order> getOrdersByStatus(OrderStatus status, Pageable pageable);
	void deleteOrder(Long id);

	OrderDetailEmployeeDTO getOrderDetails(Long orderId);
	boolean processRefund(Long orderId);

	Optional<Order> findById(Long id);

	<S extends Order> S save(S entity);
	void updateOrderStatusPaymentTime(Long orderId, String paymentTime);

    void updateOrderStatus(Long orderId);

    Double getOrderTotal(Long orderId);

	public Page<Order> getCancelledAndRefundedOrders(Pageable pageable);

	Set<PurchaseHistoryDTO> getAllOrders(Long customerId);
}
