package com.cnpm.service.impl;

import com.cnpm.entity.Product;
import com.cnpm.payload.response.ProductResponse;
import com.cnpm.repository.ProductRepository;
import com.cnpm.service.IProductService;
import com.cnpm.util.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService{
    @Autowired
    private ProductRepository productRepository;
    @Override
    public List<ProductResponse> findDistinctProduct(Pageable page) {
        List<Product> products = productRepository.findDistinctProduct(page).getContent();
        Logger.log(products.toString());
        return products.stream()
                .map(product -> {
                    ProductResponse productResponse = new ProductResponse();
                    BeanUtils.copyProperties(product, productResponse);
                    productResponse.setStock(getStockByProductCode(product.getProductCode()));
                    return productResponse;
                })
                .collect(Collectors.toList());

    }

    @Override
    public Long getStockByProductCode(String productCode) {
        Optional<Long> stock = productRepository.countAllByProductCode(productCode);
        return stock.orElse(0L);
    }
    public Long countDistinct(){
        return productRepository.countDistinct();
    }
    public Long countByCategory(String category){
        return productRepository.countByCategory(category);
    }


    public List<String> getAllCategoryName(){
        return productRepository.findDistinctCategoryName();
    }

    public List<ProductResponse> findDistinctProductsByCategory(String category, Pageable page){
        List<Product> products = productRepository.findDistinctProductsByProductCodeAndCategory(category,page).getContent();
        return products.stream()
                .map(product -> {
                    ProductResponse productResponse = new ProductResponse();
                    BeanUtils.copyProperties(product, productResponse);
                    productResponse.setStock(getStockByProductCode(product.getProductCode()));
                    return productResponse;
                })
                .collect(Collectors.toList());
    }
    public List<ProductResponse> findDistinctProductsByCategory(String category){
        List<Product> products = productRepository.findDistinctProductsByProductCodeAndCategoryWithoutPaging(category);
        return products.stream()
                .map(product -> {
                    ProductResponse productResponse = new ProductResponse();
                    BeanUtils.copyProperties(product, productResponse);
                    productResponse.setStock(getStockByProductCode(product.getProductCode()));
                    return productResponse;
                })
                .collect(Collectors.toList());
    }
}
