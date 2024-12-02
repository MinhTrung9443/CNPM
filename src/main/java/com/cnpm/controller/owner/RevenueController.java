package com.cnpm.controller.owner;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cnpm.dto.RevenueDTO;
import com.cnpm.service.interfaces.IRevenueService;

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
        
        List<RevenueDTO> revenues = revenueService.getRevenue(month);
        double totalRevenue = revenueService.getTotalRevenue(month);

        model.addAttribute("revenues", revenues);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("selectedMonth", month != null ? month : "All");

        model.addAttribute("availableMonths", revenueService.getAvailableMonths());

        return "owner/revenue";  
    }
}
