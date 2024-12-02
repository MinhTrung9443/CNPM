package com.cnpm.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.cnpm.entity.*;
import com.cnpm.service.interfaces.IProductService;
import com.cnpm.service.interfaces.IUserService;
import com.cnpm.service.facebook.FacebookService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("")
public class FacebookController {
	@Autowired
	IUserService userservice;
    @Autowired
    private IProductService productService;
    
	@GetMapping("/loginFB")
	public ModelAndView loginFB(@RequestParam String code,HttpSession session,ModelMap model) throws IOException
	{
		if (code == null || code.isEmpty())
		{
			model.addAttribute("Err", "Hệ thống đang gặp sự cố, mời đăng nhập lại hoặc sử dụng phương thức khác");
			return new ModelAndView("forward:/signin",model);
		}
		try {
			FacebookService resfb = new FacebookService();
			String accessToken = resfb.getToken(code);
	        com.restfb.types.User user = resfb.getUserInfo(accessToken);
			Account account = userservice.findByUsername(user.getId());
			if (account == null)
			{
				Customer customer = new Customer();
				customer.setFullName(user.getName());
				account = new Account();
				account.setUsername(user.getId());
				Role role = new Role();
				role.setRoleId((long) 3);
				account.setRole(role);
				customer.setAccount(account);
				
				userservice.save(customer);
				Customer newCustomer = (Customer) userservice.findByUsername(customer.getAccount().getUsername()).getUser();
				session.setAttribute("user", newCustomer);
				return new ModelAndView("redirect:/index", model);
			}
			else 
			{
				int roleid = account.getRole().getRoleId().intValue();
				if (roleid == 3) {
					Customer customer = (Customer) account.getUser();
					session.setAttribute("user", customer);
					return new ModelAndView("customer/index",model);
				}
				else if (roleid == 2)
				{
					Employee employee  = (Employee) account.getUser();
					session.setAttribute("user", employee);
					return new ModelAndView("employee/index",model);
				}
				else {
					ShopOwner owner  = (ShopOwner) account.getUser();
					session.setAttribute("user", owner);
					return new ModelAndView("owner/index",model);
				}
			}
			
		}catch (Exception e) {
			model.addAttribute("Err", "Hệ thống đang gặp sự cố, mời đăng nhập lại hoặc sử dụng phương thức khác");
			e.printStackTrace();
			return new ModelAndView("forward:/sign",model);
		}
		
	}

}
