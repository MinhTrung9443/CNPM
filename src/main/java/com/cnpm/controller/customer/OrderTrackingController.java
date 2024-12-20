package com.cnpm.controller.customer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cnpm.entity.Customer;
import com.cnpm.entity.Order;
import com.cnpm.entity.OrderLine;
import com.cnpm.entity.Payment;
import com.cnpm.entity.Product;
import com.cnpm.entity.ProductFeedback;
import com.cnpm.entity.Voucher;
import com.cnpm.service.interfaces.IOrderService;
import com.cnpm.service.interfaces.IPaymetService;
import com.cnpm.service.interfaces.IProductFeedbackService;
import com.cnpm.service.interfaces.IVoucherService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/customer")
public class OrderTrackingController {
	@Autowired
	private IOrderService orderservice;
	@Autowired
	private IProductFeedbackService reviewservice;
	@Autowired
	private IPaymetService paymentservice;
	@Autowired
	private IVoucherService voucherservice;
	@GetMapping("/followOrder/{orderId}")
	public ModelAndView followOrder(ModelMap model,HttpSession session,  @PathVariable("orderId") Long orderId)
	{
		try
		{
			Customer customer = (Customer) session.getAttribute("user");
			Product a = new Product();
			Order order = orderservice.findById(orderId).get();
	        List<ProductFeedback> list = new ArrayList<>();
	        for( OrderLine orderline : order.getOrderLines())
	        {
	        	list.addAll(reviewservice.findAllByCustomerIdAndProduct_ProductId(customer.getUserId(), orderline.getProduct().getProductId()));
	        }
	        Payment pay = paymentservice.findByOrder_OrderId(orderId);
	        Voucher voucher = voucherservice.findByOrder_OrderId(orderId);
			model.addAttribute("order", order);
			model.addAttribute("list",list);
			model.addAttribute("payment", pay);
			model.addAttribute("voucher", voucher);
			return new ModelAndView("/customer/orderDetail",model);
		}
		catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
			return new ModelAndView("customer/index",model);
		}
	}
}
