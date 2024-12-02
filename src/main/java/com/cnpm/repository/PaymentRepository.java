package com.cnpm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cnpm.entity.Order;
import com.cnpm.entity.Payment;

import io.lettuce.core.dynamic.annotation.Param;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

	@Query("SELECT p FROM Payment p "
		       + "JOIN p.order o "
		       + "JOIN o.orderLines ol "
		       + "JOIN ol.product pr "
		       + "WHERE p.paymentStatus = 'PAID' "
		       + "AND o.orderStatus <> 'CANCELLED' "
		       + "AND (:month IS NULL OR FORMAT(p.paymentDate, 'yyyy-MM') = :month) ")
		List<Payment> findPaymentsByMonth(@Param("month") String month);

	@Query("SELECT DISTINCT FORMAT(p.paymentDate, 'yyyy-MM') "
			+ "FROM Payment p ORDER BY FORMAT(p.paymentDate, 'yyyy-MM') DESC")
	List<String> findDistinctMonths();
	Payment findByOrder_OrderId(Long orderId);

    Optional<Payment> findByOrder(Order order);
	@Query("SELECT DISTINCT p FROM Payment p "
            + "JOIN p.order o "
            + "JOIN o.orderLines ol "
            + "JOIN ol.product prod "
            + "WHERE prod.category = :category")
	List<Payment> findPaymentsByCategory(String category);
	@Query("SELECT p FROM Payment p " +
		       "JOIN FETCH p.order o " +
		       "JOIN FETCH o.orderLines " +
		       "WHERE p.paymentStatus = com.cnpm.enums.PaymentStatus.PAID")	
	List<Payment> findAllPayments();
}



