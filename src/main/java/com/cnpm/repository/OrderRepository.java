package com.cnpm.repository;

import com.cnpm.entity.Order;
import com.cnpm.enums.OrderStatus;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByOrderStatus(OrderStatus status);

}
