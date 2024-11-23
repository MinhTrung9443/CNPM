package com.cnpm.service;


import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cnpm.entity.Product;
import com.cnpm.entity.ProductFeedback;

@Service
public interface IProductFeedbackService {

	<S extends ProductFeedback> S save(S entity);

	Optional<Product> findById(Long id);

}