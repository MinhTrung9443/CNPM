package com.cnpm.service.impl;

import com.cnpm.entity.Product;
import com.cnpm.entity.ProductFeedback;
import com.cnpm.repository.ProductFeedbackRepository;
import com.cnpm.repository.ProductRepository;
import com.cnpm.service.IProductFeedbackService;

import java.util.List;
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
	public Optional<Product> findById(Long id) {
		return productrepon.findById(id);
	}



	@Override
	public <S extends ProductFeedback> S save(S entity) {
		return feedbackrepo.save(entity);
	}

	// Lấy danh sách feedback dựa trên productCode
    public List<ProductFeedback> getFeedbacksByProductCode(String productCode) {
        return feedbackrepo.findAllByProduct_productCode(productCode);
    }

	@Override
	public List<ProductFeedback> findAllByCustomerIdAndProduct_ProductId(long customerId, long productId) {
		return feedbackrepo.findAllByCustomerIdAndProduct_ProductId(customerId, productId);
	}

	
	
	
}
