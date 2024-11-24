package com.cnpm.controller.customer;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cnpm.entity.Product;
import com.cnpm.repository.ProductRepository;
import com.cnpm.service.impl.ProductService;

@Controller
public class SearchProductController {
	@Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/search")
    public String searchProducts(
		@RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "minPrice", required = false) Double minPrice,
        @RequestParam(value = "maxPrice", required = false) Double maxPrice,
        @RequestParam(value = "brand", required = false) String brand,
        @RequestParam(value = "origin", required = false) String origin,
        Model model
    ) {
    	if (keyword == null) {
        keyword = ""; // hoặc có thể hiển thị tất cả sản phẩm
    	}
    	System.out.println(keyword + minPrice + maxPrice + brand + origin);        // Tìm sản phẩm theo từ khóa và bộ lọc
        List<Product> products = productService.searchProductsWithFilters(keyword, minPrice, maxPrice, brand, origin);
        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("brand", brand);
        model.addAttribute("origin", origin);
        return "customer/searchProduct";
    }
}
