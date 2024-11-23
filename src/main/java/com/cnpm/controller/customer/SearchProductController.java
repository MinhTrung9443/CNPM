package com.cnpm.controller.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cnpm.entity.Product;
import com.cnpm.service.impl.ProductService;

@Controller
public class SearchProductController {
	 @Autowired
	    private ProductService productService;

	    @GetMapping("/search")
	    public String searchProducts(@RequestParam("keyword") String keyword, Model model) {
	        // Tìm sản phẩm theo keyword
	        List<Product> products = productService.searchProducts(keyword);
	        // Đưa kết quả vào model để hiển thị
	        model.addAttribute("products", products);
	        return "customer/searchProduct";  // Tên view hiển thị kết quả tìm kiếm
	    }
}
