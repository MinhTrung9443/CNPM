package com.cnpm.controller;

import com.cnpm.payload.response.ProductResponse;
import com.cnpm.service.impl.ProductService;
import com.cnpm.service.impl.UserService;
import com.cnpm.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    UserService userService;
    @Autowired
    ProductService productService;

    @GetMapping("")
    public String home(ModelMap model, Principal principal) {
        Logger.log(principal != null ? principal.getName() : "principal is null");
        model.addAttribute("isLogin", principal != null);
        model.addAttribute("fullName", principal != null ? principal.getName() : "");
        return "web/home";
    }

    @GetMapping("/shop")
    public ModelAndView shop(ModelMap model, @RequestParam(required = false) String category, @RequestParam(required = false) Integer page, Principal principal) {


        model.addAttribute("isLogin", principal != null);
        model.addAttribute("fullName", principal != null ? principal.getName() : "");

        Logger.log(principal != null ? principal.getName() : "principal is null");
        if (category == null) {
//            lay category dau tien
            category = productService.getAllCategoryName().getFirst();
        }
        if (page == null) {
            page = 1;
        }
        Long totalProducts = productService.countByCategory(category);
        int pageSize = 8;
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
        Logger.log(category);
        Logger.log(totalPages);

        List<ProductResponse> products = productService.findDistinctProductsByCategory(category, PageRequest.of(page-1, pageSize));
        Logger.log(products);
        model.addAttribute("products", products);
        model.addAttribute("categories", productService.getAllCategoryName());
        model.addAttribute("category", category);
        model.addAttribute("page", page);
        model.addAttribute("totalPages", totalPages);

        return new ModelAndView("web/products", model);
    }
}
