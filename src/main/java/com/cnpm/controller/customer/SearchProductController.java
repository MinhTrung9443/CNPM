package com.cnpm.controller.customer;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cnpm.entity.Product;
import com.cnpm.service.interfaces.IProductService;

@Controller
public class SearchProductController {
	@Autowired
    private IProductService productService;

    @GetMapping("/search")
    public String searchProducts(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @RequestParam(value = "brand", required = false) String brand,
            @RequestParam(value = "origin", required = false) String origin,
            @RequestParam(value = "category", required = false) String category,
            Model model
    ){
        if (keyword == null) {
            keyword = ""; }
        List<Product> products = productService.searchProductsWithMultipleKeywords(keyword, minPrice, maxPrice, brand, origin, category);

        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("brand", brand);
        model.addAttribute("origin", origin);
        model.addAttribute("category", category);

        return "customer/searchProduct";
    }

}
