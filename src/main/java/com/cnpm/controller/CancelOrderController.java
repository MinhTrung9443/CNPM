package com.cnpm.controller;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cnpm.entity.Order;
import com.cnpm.entity.OrderLine;
import com.cnpm.entity.Product;
import com.cnpm.enums.OrderStatus;
import com.cnpm.service.IOrderService;
import com.cnpm.service.IProductService;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.server.PathParam;


@Controller
@RequestMapping("")
public class CancelOrderController implements Serializable{
	@Autowired 
	private IOrderService orderservice;
	@Autowired
	private IProductService productservice;
	@GetMapping("/cancelOrder/{orderId}")
	public ModelAndView cancelOrder(HttpSession session, ModelMap model, @PathVariable("orderId") Long orderId)
	{
		Optional<Order> opOrder = orderservice.findById(orderId);
		Order order = new Order();
		try
		{
			order = opOrder.get();
			return new ModelAndView("redirect:/followOrder/100",model);
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		order.setOrderStatus(OrderStatus.CANCELLED);
		orderservice.save(order);
		
		for (OrderLine orderline : order.getOrderLines())
		{
			
			int quantity =  orderline.getQuantity();
			Product pro = productservice.findById(orderline.getProduct().getProductId()).get();
			pro.setProductId(null);
			pro.setIsUsed(0);
			for (int i = 0;i<quantity;i++)
			{
				productservice.save(pro);
			}
			
		}
		return new ModelAndView("",model);

	}
}
