package com.cnpm.repository;

import com.cnpm.entity.Order;
import com.cnpm.entity.Payment;
import jakarta.validation.Valid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrder(Order order);

    Optional<Payment> findByOrder_OrderId(@Valid Long orderId);
}
