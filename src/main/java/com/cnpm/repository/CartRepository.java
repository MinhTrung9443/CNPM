package com.cnpm.repository;

import com.cnpm.entity.Cart;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
	Optional<Cart> findByCustomerUserId(Long id);
}
