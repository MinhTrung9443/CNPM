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
import com.cnpm.service.impl.ProductFeedbackService;
import com.cnpm.service.impl.ProductService;

@Controller
@RequestMapping("/product")
public class ProductDetailController {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductFeedbackService productFeedbackService;

    @GetMapping("/{productId}")
    public String viewProductDetail(@PathVariable("productId") Long productId, Model model) {
        // Lấy thông tin sản phẩm dạng DTO
        ProductDTO productDTO = productService.getProductDTOById(productId);

        if (productDTO != null) {
            // Lấy danh sách feedback dựa trên productCode
            List<ProductFeedback> productFeedbacks = productFeedbackService.getFeedbacksByProductCode(productDTO.getProductCode());
            productDTO.setProductFeedbacks(productFeedbacks);

            // Đưa sản phẩm và feedbacks vào model
            model.addAttribute("product", productDTO);
            model.addAttribute("feedbacks", productFeedbacks);

            return "customer/productDetail"; // View chi tiết sản phẩm
        } else {
            return "err/error"; // Trang báo lỗi nếu không tìm thấy sản phẩm
        }
    }
}


