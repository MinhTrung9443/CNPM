package com.cnpm.repository;

import com.cnpm.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByOrder_OrderId(Long orderId);
}
