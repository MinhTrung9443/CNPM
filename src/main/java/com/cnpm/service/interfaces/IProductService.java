package com.cnpm.service.interfaces;

import java.util.List;
import java.util.Optional;

import com.cnpm.entity.ProductFeedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cnpm.dto.ProductResponse;
import com.cnpm.entity.Product;



@Service
public interface IProductService {
    List<ProductResponse> findDistinctProduct(Pageable page);
    Long getStockByProductCode(String productCode);

    Long countByCategory(String category);

    List<Product> searchProducts(String keyword);

    ProductResponse getProductDTOById(Long productId);

    List<ProductFeedback> getProductFeedbacksByProductId(String productCode);

    List<Product> searchProductsWithMultipleKeywords(String keywords, Double minPrice, Double maxPrice, String brand, String origin, String category);

    List<Product> findAll();
	void save(Product product);
	Optional<Product> findById(long id);
	void delete(Product product);
	List<Product> findAllDistinctProduct();
	List<Product> findAllProductsByProductCode(String productCode);
	Long count();

    Page<Product> findProductsByCategory(String category, Pageable page);

    long countDistinctProductsByCategory(String category);

    List<ProductResponse> searchProductsWithName(String query, Pageable page);
	long countDistinctProducts(String productName);

    Product getProductByProductCode(String productCode);
}
