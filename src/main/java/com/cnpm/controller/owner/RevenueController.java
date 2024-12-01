package com.cnpm.controller.owner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cnpm.service.interfaces.IRevenueService;

@Controller
@RequestMapping("/owner")
public class RevenueController {

    @Autowired
    private IRevenueService revenueService;

    @GetMapping("/revenue-list")
    public String showRevenuePage(
            @RequestParam(value = "month", required = false) String month,
            @RequestParam(value = "category", required = false) String category,
            Model model) {

    	 double totalRevenue = revenueService.getTotalRevenue(month, category);
         model.addAttribute("totalRevenue", totalRevenue);
         
        // Gọi service với các tham số
        model.addAttribute("revenues", revenueService.getRevenue(month, category));
        model.addAttribute("selectedMonth", month);
        model.addAttribute("selectedCategory", category);
        return "owner/revenue-list";
    }
}
