package com.cnpm.service.impl;

import com.cnpm.dto.ProductDTO;
import com.cnpm.entity.Product;
import com.cnpm.entity.ProductFeedback;
import com.cnpm.repository.ProductFeedbackRepository;
import com.cnpm.dto.ProductResponse;
import com.cnpm.repository.ProductRepository;
import com.cnpm.service.IProductService;
import com.cnpm.util.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService{
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductFeedbackRepository productFeedbackRepository;
    @Override
    public List<ProductResponse> findDistinctProduct(Pageable page) {
//        List<Product> products = productRepository.findAll();
//        List<Product> distinctProducts = products.stream()
//                .collect(Collectors.groupingBy(Product::getProductCode))
//                .values()
//                .stream()
//                .map(List::getFirst)
//                .toList();
//        return distinctProducts.stream()
//                .map(product -> {
//                    ProductResponse productResponse = new ProductResponse();
//                    BeanUtils.copyProperties(product, productResponse);
//                    productResponse.setStock(getStockByProductCode(product.getProductCode()));
//                    return productResponse;
//                })
//                .collect(Collectors.toList());
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
	public Long count(){
        return productRepository.countDistinct();
    }
    public Long countByCategory(String category){
        return productRepository.countByCategory(category);
    }

    public List<Product> searchProducts(String keyword) {
        // Chỉ tìm kiếm các sản phẩm chưa được mua
        return productRepository.findDistinctProductsByKeyword(keyword);
    }


    
    public ProductDTO getProductDTOById(Long productId) {
        // Lấy thông tin sản phẩm từ bảng Product
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            // Tạo đối tượng ProductDTO
            ProductDTO productDTO = new ProductDTO();
//            productDTO.setProductCode(product.getProductCode());
//            productDTO.setProductName(product.getProductName());
//            productDTO.setCategory(product.getCategory());
//            productDTO.setCost(product.getCost());
//            productDTO.setDescription(product.getDescription());
//            productDTO.setBrand(product.getBrand());
//            productDTO.setManufactureDate(product.getManufactureDate());
//            productDTO.setExpirationDate(product.getExpirationDate());
//            productDTO.setIngredient(product.getIngredient());
//            productDTO.setHow_to_use(product.getHow_to_use());
//            productDTO.setVolume(product.getVolume());
//            productDTO.setOrigin(product.getOrigin());
//            productDTO.setImage(product.getImage());
            
            BeanUtils.copyProperties(product, productDTO);
            
            

            // Lấy tồn kho từ productCode
            Long stock = getStockByProductCode(product.getProductCode());
            productDTO.setStock(stock);

            return productDTO;
        }
        return null; // Trả về null nếu không tìm thấy sản phẩm
    }
    public List<ProductFeedback> getProductFeedbacksByProductId(String productCode) {
        // Lấy danh sách nhận xét từ ProductFeedback
        return productFeedbackRepository.findAllByProduct_productCode(productCode);
    }
 

    public List<Product> searchProductsWithFilters(String keyword, Double minPrice, Double maxPrice, String brand, String origin, String category) {
        // Lấy danh sách sản phẩm theo từ khóa
        List<Product> products = productRepository.findDistinctProductsByKeyword(keyword);

        // Áp dụng bộ lọc
        if (minPrice != null) {
            products = products.stream()
                               .filter(p -> p.getCost() >= minPrice)
                               .collect(Collectors.toList()); }
        
        if (maxPrice != null) {
            products = products.stream()
                               .filter(p -> p.getCost() <= maxPrice)
                               .collect(Collectors.toList()); }
        if (brand != null && brand != "") {
            products = products.stream()
                               .filter(p -> p.getBrand().equalsIgnoreCase(brand))
                               .collect(Collectors.toList());}
        if (origin != null && origin != "") {
            products = products.stream()
                               .filter(p -> p.getOrigin().equalsIgnoreCase(origin))
                               .collect(Collectors.toList()); }
        if (category != null && category != "") {
            products = products.stream()
                               .filter(p -> p.getCategory().equalsIgnoreCase(category))
                               .collect(Collectors.toList()); }
        return products;
    }
    


    @Override
	public List<Product> findAll()
    {
    	return productRepository.findAll();
    }
    
    @Override
	public void save(Product product)
    {
    	productRepository.save(product);
    }
    
    @Override
	public Optional<Product> findById(long id)
    {
    	return productRepository.findById(id);
    }
    
    @Override
	public void delete(Product product)
    {
    	productRepository.delete(product);
    }
    
    @Override
	public List<Product> findAllDistinctProduct()
    {
    	return productRepository.findDistinctProductsByProductCode();
    }
    
    @Override
	public List<Product> findAllProductsByProductCode(String productCode)
    {
    	return productRepository.findProductsByProductCode(productCode);
    }
}
