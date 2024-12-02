package com.cnpm.controller.owner;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cnpm.entity.Payment;
import com.cnpm.service.IRevenueService;

@Controller
@RequestMapping("/owner")
public class RevenueController {

    @Autowired
    private IRevenueService revenueService;

    @GetMapping("/revenue")
    public String getRevenueList(
            @RequestParam(value = "month", required = false) String month,
            @RequestParam(value = "category", required = false) String category,
            Model model) {

        
        if ("All".equals(month)) {
            month = null;
        }
        
        List<Payment> payments = revenueService.getRevenue(month);
        double totalPayment = revenueService.getTotalRevenue(month);

        model.addAttribute("payments", payments);
        model.addAttribute("totalPayment", totalPayment);
        model.addAttribute("selectedMonth", month != null ? month : "All");

        model.addAttribute("availableMonths", revenueService.getAvailableMonths());

        return "owner/revenue";  
    }
}
