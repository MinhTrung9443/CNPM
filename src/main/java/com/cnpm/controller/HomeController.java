package com.cnpm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cnpm.dto.ProductResponse;
import com.cnpm.entity.Product;
import com.cnpm.service.impl.ProductService;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    private ProductService productService;

    @GetMapping({"", "/index", "/index.html", "/customer/index.html", "/Customer*/index", "/page"})
    public String showHomePage(Model model,@RequestParam(value = "pageNo", defaultValue = "1") int pageNo) {
    	
	    int pageSize = 25;
	    if (pageNo <= 0) {
	        pageNo = 1;
	    }
	    Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
	    
	    List<ProductResponse> productResponses = productService.findDistinctProduct(pageable);

	    model.addAttribute("currentPage", pageNo);
	    model.addAttribute("products", productResponses);
	    model.addAttribute("pageSize", pageSize);
	    model.addAttribute("totalItems", productService.count());
	    model.addAttribute("totalPages", (int) Math.ceil((double) productService.count() / pageSize));
        return "customer/index";
    }
    @GetMapping({"/employee/index.html", "/employee*/index"})
    public String showEmployeePage(Model model) {
        // Lấy danh sách tất cả sản phẩm
        List<Product> allProducts = productService.findAllDistinctProduct();
        model.addAttribute("products", allProducts);
        return "Employee/index";
    }
    
    @GetMapping("/products")
    public String showProductsByCategory(
        @RequestParam(value = "category", required = false) String category,
        Model model) {
        
        List<Product> products;

        if (category != null && !category.isEmpty()) {
            // Lấy danh sách sản phẩm theo thể loại
            products = productService.findProductsByCategory(category);
        } else {
            // Lấy danh sách tất cả sản phẩm
            products = productService.findAllDistinctProduct();
        }
        
        model.addAttribute("products", products);
        model.addAttribute("selectedCategory", category);
        return "customer/index";
    }


    @GetMapping("/gioithieu")
    public String showAboutPage() {
        return "gioithieu"; // Tên của tệp HTML trong thư mục templates
    }
}
