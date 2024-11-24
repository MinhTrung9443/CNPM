package com.cnpm.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cnpm.entity.Customer;
import com.cnpm.entity.Product;
import com.cnpm.entity.ProductFeedback;
import com.cnpm.service.IProductFeedbackService;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("")
public class ReviewController {
	@Autowired
	private IProductFeedbackService feedbackservice;
	@PostMapping("/review")
	public ModelAndView feedback(ModelMap model,HttpSession session, @ModelAttribute ProductFeedback feedback ,@RequestParam("productId") Long productId)
	{
		Customer customer = (Customer) session.getAttribute("user");
		feedback.setFeedbackDate(LocalDateTime.now());
		feedback.setCustomerId(customer.getUserId());
		Product pro = feedbackservice.findById(productId).get();
		for (int i = 0;i<10;i++)
		System.out.println("ALLLLLLLLLLLLLLLLLLLLL" + feedback.toString());
		if (pro != null)
		{
			feedback.setProduct(pro);
			feedbackservice.save(feedback);
			return new ModelAndView("redirect:/followOrder/100",model);
		}
		model.addAttribute("Err", "Hệ thống đang gặp lỗi, mời thử lại sau!!!");
		return new ModelAndView("redirect:/followOrder/100",model);
	}
}
