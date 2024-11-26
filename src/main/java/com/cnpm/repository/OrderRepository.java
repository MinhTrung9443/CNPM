package com.cnpm.repository;

import com.cnpm.entity.Order;
import com.cnpm.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Set<Order> findByCustomerId(Long customerId);
    List<Order> findByOrderStatus(OrderStatus status);
    Set<Order> findByCustomerIdAndOrderStatus(Long customerId, OrderStatus orderStatus);
}
