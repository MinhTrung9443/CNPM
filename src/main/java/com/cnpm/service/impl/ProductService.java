package com.cnpm.service.impl;

import com.cnpm.entity.Product;
import com.cnpm.entity.ProductFeedback;
import com.cnpm.repository.ProductFeedbackRepository;
import com.cnpm.dto.ProductResponse;
import com.cnpm.repository.ProductRepository;
import com.cnpm.service.interfaces.IProductService;
import com.cnpm.util.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductFeedbackRepository productFeedbackRepository;

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
        Optional<Long> stock = productRepository.countAllByProductCodeAndIsUsedFalse(productCode);
        return stock.orElse(0L);
    }

    @Override
    public Long count() {
        return productRepository.countDistinct();
    }

    @Override
    public Long countByCategory(String category) {
        return productRepository.countByCategory(category);
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        // Chỉ tìm kiếm các sản phẩm chưa được mua
        return productRepository.findDistinctProductsByKeyword(keyword);
    }


    @Override
    public ProductResponse getProductDTOById(Long productId) {
        // Lấy thông tin sản phẩm từ bảng Product
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            // Tạo đối tượng ProductDTO
            ProductResponse productDTO = new ProductResponse();

            BeanUtils.copyProperties(product, productDTO);

            // Lấy tồn kho từ productCode
            Long stock = getStockByProductCode(product.getProductCode());
            productDTO.setStock(stock);

            return productDTO;
        }
        return null;
    }

    @Override
    public List<ProductFeedback> getProductFeedbacksByProductId(String productCode) {
        return productFeedbackRepository.findAllByProduct_productCode(productCode);
    }


    @Override
    public List<Product> searchProductsWithMultipleKeywords(String keywords, Double minPrice, Double maxPrice, String brand, String origin, String category) {
        List<String> keywordList = List.of(keywords.split("\\s+"));

        List<Product> filteredProducts = keywordList.stream()
                .flatMap(keyword -> productRepository.findDistinctProductsBySingleKeyword(keyword).stream())
                .distinct()
                .collect(Collectors.toList());

        if (minPrice != null) {
            filteredProducts = filteredProducts.stream()
                    .filter(p -> p.getCost() >= minPrice)
                    .collect(Collectors.toList());
        }
        if (maxPrice != null) {
            filteredProducts = filteredProducts.stream()
                    .filter(p -> p.getCost() <= maxPrice)
                    .collect(Collectors.toList());
        }
        if (brand != null && !brand.isEmpty()) {
            filteredProducts = filteredProducts.stream()
                    .filter(p -> p.getBrand().equalsIgnoreCase(brand))
                    .collect(Collectors.toList());
        }
        if (origin != null && !origin.isEmpty()) {
            filteredProducts = filteredProducts.stream()
                    .filter(p -> p.getOrigin().equalsIgnoreCase(origin))
                    .collect(Collectors.toList());
        }
        if (category != null && !category.isEmpty()) {
            filteredProducts = filteredProducts.stream()
                    .filter(p -> p.getCategory().equalsIgnoreCase(category))
                    .collect(Collectors.toList());
        }
        return filteredProducts;
    }


    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }

    @Override
    public Optional<Product> findById(long id) {
        return productRepository.findById(id);
    }

    @Override
    public void delete(Product product) {
        productRepository.delete(product);
    }

    @Override
    public List<Product> findAllDistinctProduct() {
        return productRepository.findDistinctProductsByProductCode();
    }

    @Override
    public List<Product> findAllProductsByProductCode(String productCode) {
        return productRepository.findProductsByProductCode(productCode);
    }

    @Override
    public Page<Product> findProductsByCategory(String category, Pageable page) {
        return productRepository.findDistinctProductsByCategory(category, page);
    }

    @Override
    public long countDistinctProductsByCategory(String category) {
        return productRepository.countDistinctProductsByCategory(category);
    }
    // Tìm kiếm sản phẩm theo tên hoặc mô tả

    @Override
    public List<ProductResponse> searchProductsWithName(String query, Pageable page) {
        List<Product> products = productRepository.findDistinctProductsByProductNameContaining(query, page)
                .getContent();
        Logger.log(products.toString());
        return products.stream().map(product -> {
            ProductResponse productResponse = new ProductResponse();
            BeanUtils.copyProperties(product, productResponse);
            productResponse.setStock(getStockByProductCode(product.getProductCode()));
            return productResponse;
        }).collect(Collectors.toList());

    }

    // dem san pham duoc lco theo ten

    @Override
    public long countDistinctProducts(String productName) {
        return productRepository.countDistinctProductsByProductNameContaining(productName);
    }

    @Override
    public Product getProductByProductCode(String productCode) {
        return productRepository.findFirstByProductCode(productCode).orElseThrow(() -> new RuntimeException("Product " + productCode + " not found"));
    }
    
    //ktra productcode
    @Override
	public boolean existsByProductCode(String productCode) {
        return productRepository.existsByProductCode(productCode);
    }
}
