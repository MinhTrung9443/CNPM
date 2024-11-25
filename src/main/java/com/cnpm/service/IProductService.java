package com.cnpm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cnpm.dto.ProductResponse;
import com.cnpm.entity.Product;



@Service
public interface IProductService {
    List<ProductResponse> findDistinctProduct(Pageable page);
    Long getStockByProductCode(String productCode);
	List<Product> findAll();
	void save(Product product);
	Optional<Product> findById(long id);
	void delete(Product product);
	List<Product> findAllDistinctProduct();
	List<Product> findAllProductsByProductCode(String productCode);
	Long count();

}
