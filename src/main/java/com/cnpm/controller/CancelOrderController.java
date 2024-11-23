package com.cnpm.controller;

import java.awt.print.Pageable;
import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
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
import selogger.net.bytebuddy.description.modifier.EnumerationState;

@Controller
@RequestMapping("/cancel")
public class CancelOrderController implements Serializable{
	@Autowired 
	private IOrderService orderservice;
	@Autowired
	private IProductService productservice;
	@GetMapping
	public ModelAndView cancelOrder(HttpSession session, ModelMap model, @PathParam("orderId") Long orderId)
	{
		Order order = orderservice.findById(orderId).get();
		order.setOrderStatus(OrderStatus.CANCELLED);
		orderservice.save(order);
		
		for (OrderLine orderline : order.getOrderLines())
		{
			
			PageRequest page = PageRequest.of(0, orderline.getQuantity());
			Product pro = productservice.findById(orderline.getProduct().getProductId()).get();
			Page<Product> product = productservice.findProductsByCodeAndIsUsedAndProductId(pro.getProductCode(),pro.getProductId() , page);
			for (Product prod : product)
			{
				prod.setIsUsed(0);
				productservice.save(prod);
			}
		}
		return new ModelAndView("",model);
	}
}
