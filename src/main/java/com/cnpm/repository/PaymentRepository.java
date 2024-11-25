package com.cnpm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cnpm.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
	@Query("SELECT p FROM Payment p " +
		       "JOIN FETCH p.order o " +
		       "JOIN FETCH o.orderLines " +
		       "WHERE p.paymentStatus = com.cnpm.enums.PaymentStatus.PAID")	
	List<Payment> findAllPayments();

    @Query("SELECT p FROM Payment p WHERE FUNCTION('DATE_FORMAT', p.paymentDate, '%Y-%m') = :month")
	List<Payment> findPaymentsByMonth(String month);

    
    @Query("SELECT DISTINCT p FROM Payment p "
            + "JOIN p.order o "
            + "JOIN o.orderLines ol "
            + "JOIN ol.product prod "
            + "WHERE prod.category = :category")
	List<Payment> findPaymentsByCategory(String category);

    Payment findByOrder_OrderId(Long orderId);
}



