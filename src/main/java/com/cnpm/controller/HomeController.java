package com.cnpm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cnpm.entity.Product;
import com.cnpm.service.impl.ProductService;

@Controller
@RequestMapping("")
public class HomeController {
    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String showHomePage(Model model) {
        // Lấy danh sách tất cả sản phẩm
        List<Product> allProducts = productService.findAllDistinctProduct();
        model.addAttribute("products", allProducts);
        return "customer/index";
    }
}
