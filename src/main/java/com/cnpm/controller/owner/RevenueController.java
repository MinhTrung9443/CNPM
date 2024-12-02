package com.cnpm.controller.owner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cnpm.service.IRevenueService;

@Controller
@RequestMapping("/owner")
public class RevenueController {

    @Autowired
    private IRevenueService revenueService;

    @GetMapping("/revenue")
    public String showRevenuePage(
            @RequestParam(value = "month", required = false) String month,
            @RequestParam(value = "category", required = false) String category,
            Model model) {

        System.out.println("Selected Month: " + month);
        System.out.println("Selected Category: " + category);

        double totalRevenue = revenueService.getTotalRevenue(month, category);
        model.addAttribute("totalRevenue", totalRevenue);

        model.addAttribute("revenues", revenueService.getRevenue(month, category));
        model.addAttribute("selectedMonth", month);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("availableMonths", revenueService.getAvailableMonths());
        model.addAttribute("availableCategories", revenueService.getAvailableCategory());

        return "owner/revenue";
    }

}
