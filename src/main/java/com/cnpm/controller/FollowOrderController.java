package com.cnpm.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.cnpm.entity.Customer;
import com.cnpm.entity.Order;
import com.cnpm.entity.OrderLine;
import com.cnpm.entity.Product;
import com.cnpm.entity.ProductFeedback;
import com.cnpm.enums.OrderStatus;
import com.cnpm.service.IOrderService;
import com.cnpm.service.IProductFeedbackService;
import com.cnpm.service.IUserService;

import jakarta.websocket.server.PathParam;

@Controller
public class FollowOrderController {
	@Autowired
	private IOrderService orderservice;
	@Autowired
	private IUserService userservice;
	@Autowired
	private IProductFeedbackService reviewservice;
	@GetMapping("/followOrder/{orderId}")
	public ModelAndView followOrder(ModelMap model,  @PathVariable("orderId") Long orderId)
	{
		System.out.println(orderId);
		Customer customer = (Customer) userservice.findByEmail("minhtrungbttv@gmail.com");
		
		Product product1 = new Product();
		product1.setProductId((long)1);
        product1.setProductCode("P001");
        product1.setProductName("Product 1");
        product1.setCost(100.0);
        product1.setCategory("Category A");
        product1.setBrand("Brand X");
        product1.setImage("https://d1hjkbq40fs2x4.cloudfront.net/2017-08-21/files/landscape-photography_1645-t.jpg");

        Product product2 = new Product();
        product2.setProductId((long)2);
        product2.setProductCode("P002");
        product2.setProductName("Product 2");
        product2.setCost(200.0);
        product2.setCategory("Category B");
        product2.setBrand("Brand Y");
        product2.setImage("https://d1hjkbq40fs2x4.cloudfront.net/2017-08-21/files/landscape-photography_1645-t.jpg");



        OrderLine orderLine1 = new OrderLine();
        orderLine1.setProduct(product1);
        orderLine1.setQuantity(2);

        OrderLine orderLine2 = new OrderLine();
        orderLine2.setProduct(product2);
        orderLine2.setQuantity(1);


        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress("123 Main St, Hanoi");
        order.setOrderStatus(OrderStatus.COMPLETED);
        order.setDeliveryDate(LocalDateTime.now().plusDays(3));
        order.setCustomer(customer);


        Set<OrderLine> orderLines = new HashSet<>();
        orderLines.add(orderLine1);
        orderLines.add(orderLine2);
        order.setOrderLines(orderLines);


        orderLine1.setOrder(order);
        orderLine2.setOrder(order);

//		Order order = orderservice.findById(orderId).get();
        List<ProductFeedback> list = new ArrayList<>();
        for( OrderLine orderline : order.getOrderLines())
        {
        	list.addAll(reviewservice.findAllByCustomerIdAndProduct_ProductId(customer.getUserId(), orderline.getProduct().getProductId()));
        }
        System.out.println(list.toString());
        
		order.setOrderId((long)100);
		model.addAttribute("order", order);
		model.addAttribute("list",list);
		return new ModelAndView("/customer/orderDetail");
	}
}
