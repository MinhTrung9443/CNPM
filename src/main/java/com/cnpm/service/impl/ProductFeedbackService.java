package com.cnpm.service.impl;

import com.cnpm.entity.Product;
import com.cnpm.entity.ProductFeedback;
import com.cnpm.repository.ProductFeedbackRepository;
import com.cnpm.repository.ProductRepository;
import com.cnpm.service.IProductFeedbackService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductFeedbackService implements IProductFeedbackService {
	@Autowired
	ProductRepository productrepon;
	@Autowired
	ProductFeedbackRepository feedbackrepo;

	@Override
	public Optional<Product> findByProductCode(String productCode) {
		return productrepon.findByProductCode(productCode);
	}

	@Override
	public <S extends ProductFeedback> S save(S entity) {
		return feedbackrepo.save(entity);
	}

	
	
	
}
