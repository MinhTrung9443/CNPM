package com.cnpm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cnpm.dto.ProductResponse;
import com.cnpm.entity.Product;
import com.cnpm.entity.Voucher;
import com.cnpm.service.interfaces.IVoucherService;
import com.cnpm.service.impl.ProductService;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    private ProductService productService;
    @Autowired
    private IVoucherService voucherService;

    @GetMapping({"", "/index", "/index.html", "/customer/index.html", "/customer/index", "/page"})
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
    
    @GetMapping({"owner/index.html", "owner/index"})
    public String showOwnerPage(Model model) {
        return "owner/index";
    }
    
    @GetMapping({"/products","/product/page"})
    public String showProductsByCategory(@RequestParam(value = "category", required = false) String category,Model model,
    		@RequestParam(value = "pageNo", defaultValue = "1") int pageNo) {
    	int pageSize = 10;
	    if (pageNo <= 0) {
	        pageNo = 1;
	    }
	    Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
	    long count = 0;
        if (category != null && !category.isEmpty()) {
            // Lấy danh sách sản phẩm theo thể loại
        	Page<Product> products = productService.findProductsByCategory(category, pageable);
        	model.addAttribute("products", products);
        	count = productService.countDistinctProductsByCategory(category);
        	for (int i = 0;i<10;i++)
        		System.out.println("so luong " + count);
            
        } 
        else {
            // Lấy danh sách tất cả sản phẩm
            List<ProductResponse> products = productService.findDistinctProduct(pageable);
            model.addAttribute("products", products);
            count = productService.count();
        }
        model.addAttribute("selectedCategory", category);
        model.addAttribute("currentPage", pageNo);
	    model.addAttribute("pageSize", pageSize);
	    model.addAttribute("totalItems", count);
	    model.addAttribute("totalPages", (int) count / pageSize);
        return "customer/productCategory";
    }


    @GetMapping("/gioithieu")
    public String showAboutPage() {
        return "gioithieu"; // Tên của tệp HTML trong thư mục templates
    }
    
    @GetMapping("ViewVoucher")
    public String ShowVoucher(ModelMap model) {
    	List<Voucher> vouchers = voucherService.findAllByIsUsedFalseAndStartDateBeforeAndEndDateAfter();
    	model.addAttribute("listVoucher", vouchers);
    	return "customer/viewVoucher";
    }
    
}
