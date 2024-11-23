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
    public <S extends ProductFeedback> S save(S entity) {
        return feedbackrepo.save(entity);
    }

    // Lấy danh sách feedback dựa trên productCode
    public List<ProductFeedback> getFeedbacksByProductCode(String productCode) {
        return feedbackrepo.findAllByProduct_productCode(productCode);
    }

	@Override
	public Optional<Product> findByProductCode(String productCode) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	
	
	
}
