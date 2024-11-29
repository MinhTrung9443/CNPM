package com.cnpm.repository;

import com.cnpm.dto.PurchaseHistoryDTO;
import com.cnpm.entity.Order;
import com.cnpm.enums.OrderStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT new com.cnpm.dto.PurchaseHistoryDTO(o.orderId, o.orderDate, o.shippingAddress, o.orderStatus, " +
           "(SELECT new com.cnpm.dto.ProductHistoryDTO(ol.product.productId,ol.product.productCode, ol.product.productName, ol.quantity, ol.product.image, ol.product.cost, ol.product.category) " +
           "FROM OrderLine ol WHERE ol.order = o), " +
           "(SELECT SUM(ol.product.cost * ol.quantity) FROM OrderLine ol WHERE ol.order = o)) " +
           "FROM Order o WHERE o.customerId = :customerId")
	Set<PurchaseHistoryDTO> getAllOrders(Long customerId); 
	Page<Order> findByCustomerId(Long customerId, Pageable pageable);

	Set<Order> findByCustomerId(Long customerId);

	List<Order> findByOrderStatus(OrderStatus status);

	Set<Order> findByCustomerIdAndOrderStatus(Long customerId, OrderStatus orderStatus);

	// Method to find orders by status with pagination support
	Page<Order> findByOrderStatus(OrderStatus orderStatus, Pageable pageable);

	// If you need to support multiple statuses (like cancelled and refunded), you
	// can add:
	Page<Order> findByOrderStatusIn(List<OrderStatus> statuses, Pageable pageable);

}
