package com.cnpm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cnpm.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

	@Query("SELECT p FROM Payment p "
		       + "JOIN p.order o "
		       + "JOIN o.orderLines ol "
		       + "JOIN ol.product pr "
		       + "WHERE p.paymentStatus = 'PAID' "
		       + "AND o.orderStatus != 'CANCELLED' "
		       + "AND (:month IS NULL OR FORMAT(p.paymentDate, 'yyyy-MM') = :month) "
		       + "AND (:category IS NULL OR pr.category = :category)")
		List<Payment> findPaymentsByFilters(@Param("month") String month, @Param("category") String category);

	@Query("SELECT DISTINCT FORMAT(p.paymentDate, 'yyyy-MM') "
			+ "FROM Payment p ORDER BY FORMAT(p.paymentDate, 'yyyy-MM') DESC")
	List<String> findDistinctMonths();

	@Query("SELECT DISTINCT ol.product.category FROM OrderLine ol")
	List<String> findAllCategories();
}
