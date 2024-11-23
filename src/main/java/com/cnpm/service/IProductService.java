package com.cnpm.service;

import com.cnpm.entity.Product;
import com.cnpm.payload.response.ProductResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IProductService {
    List<ProductResponse> findDistinctProduct(Pageable page);
    Long getStockByProductCode(String productCode);
	Page<Product> findProductsByCodeAndIsUsedAndProductId(String productCode, Long productId, Pageable pageable);
	Optional<Product> findById(Long id);
	<S extends Product> S save(S entity);
    
}
