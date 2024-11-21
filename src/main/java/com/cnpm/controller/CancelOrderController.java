package com.cnpm.controller;

import java.io.Serializable;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.server.PathParam;

@Controller
@RequestMapping("/cancel")
public class CancelOrderController implements Serializable{
	@GetMapping
	public ModelAndView cancelOrder(HttpSession session, ModelMap model, @PathParam("orderId") Long orderId)
	{
		return new ModelAndView("",model);
	}
}
