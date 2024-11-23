package com.cnpm.controller.customer;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cnpm.dto.ProductDTO;
import com.cnpm.entity.Product;
import com.cnpm.entity.ProductFeedback;
import com.cnpm.service.impl.ProductService;

@Controller
@RequestMapping("/product")
public class ProductDetailController {
    @Autowired
    private ProductService productService;

    @GetMapping("/{productId}")
    public String viewProductDetail(@PathVariable("productId") String productId, Model model) {
        // Lấy thông tin sản phẩm dạng DTO
        ProductDTO productDTO = productService.getProductDTOById(Long.valueOf(productId));

        if (productDTO!= null) {
            List<ProductFeedback> productFeedbacks = productService.getProductFeedbacksByProductId(productDTO.getProductCode());
            productDTO.setProductFeedbacks(productFeedbacks);
            model.addAttribute("product", productDTO);
            return "customer/productDetail";
        } else {
            return "err/error";  // Trang báo lỗi nếu không tìm thấy sản phẩm
        }
}}

