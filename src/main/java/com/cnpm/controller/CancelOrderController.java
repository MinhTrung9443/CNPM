package com.cnpm.controller;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cnpm.entity.Order;
import com.cnpm.service.IOrderService;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.server.PathParam;

@Controller
@RequestMapping("/cancel")
public class CancelOrderController implements Serializable{
	@Autowired 
	private IOrderService orderservice;
	@GetMapping
	public ModelAndView cancelOrder(HttpSession session, ModelMap model, @PathParam("orderId") Long orderId)
	{
//		Order order = orderservice.findById(orderId);
//		order.setOrderStatus("CANCEL");
//		orderservice.save(order);
		return new ModelAndView("",model);
	}
}
