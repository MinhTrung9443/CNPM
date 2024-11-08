package com.cnpm.controller;

import com.cnpm.payload.response.ProductResponse;
import com.cnpm.repository.ProductRepository;
import com.cnpm.service.impl.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;
    @ResponseBody
    @GetMapping("/list")
    public List<ProductResponse> list(@RequestParam(defaultValue = "0") Integer page) {
        if (page == null) {
            page = 0;
        }
        int totalProducts = productService.count().intValue();
        int pageSize = 10;
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
        int pageNumber = page; // trang đầu tiên
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("productCode").ascending());
        List<ProductResponse> productPage = productService.findDistinctProduct(pageable);

        return productPage;
    }
    @GetMapping("")
    public String index() {
        return "admin/products";
    }
}
