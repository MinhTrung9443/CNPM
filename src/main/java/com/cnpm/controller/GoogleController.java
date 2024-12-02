package com.cnpm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cnpm.dto.google.GooglePojo;
import com.cnpm.entity.Account;
import com.cnpm.entity.Customer;
import com.cnpm.entity.Employee;
import com.cnpm.entity.Product;
import com.cnpm.entity.Role;
import com.cnpm.entity.ShopOwner;
import com.cnpm.entity.User;
import com.cnpm.service.google.GoogleService;
import com.cnpm.service.impl.UserService;
import com.cnpm.service.interfaces.IProductService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("")

public class GoogleController {
	@Autowired
	private GoogleService googleUtils;
	@Autowired
	private UserService userservice;
    @Autowired
    private IProductService productService;

	@RequestMapping("/loginGG")
	public ModelAndView LoginWithGoogle(@RequestParam String code, HttpSession session, ModelMap model) {
		if (code == null || code.isEmpty()) {
			model.addAttribute("Err", "Hệ thống đang gặp sự cố, mời đăng nhập lại hoặc sử dụng phương thức khác");
			return new ModelAndView("forward:/signin",model);
		}

		try {

			String accessToken = googleUtils.getToken(code);

			GooglePojo googleUser = googleUtils.getUserInfo(accessToken);

			User user = userservice.findByEmail(googleUser.getEmail());

			if (user == null) {
				Customer customer = new Customer();
				customer.setEmail(googleUser.getEmail());
				customer.setFullName(googleUser.getName());
				Account account = new Account();
				Role role = new Role();
				role.setRoleId((long) 3);
				account.setRole(role);
				customer.setAccount(account);
				userservice.save(customer);
				
				Customer newCustomer = (Customer) userservice.findByEmail(customer.getEmail());
				session.setAttribute("user", newCustomer);

				return new ModelAndView("customer/index",model);
			} else {
				int roleid = user.getAccount().getRole().getRoleId().intValue();
				if (roleid == 3) {
					Customer customer = (Customer) user;
					session.setAttribute("user", customer);
					return new ModelAndView("redirect:/index", model);
				}
				else if (roleid == 2)
				{
					Employee employee  = (Employee) user;
					session.setAttribute("user", employee);
					return new ModelAndView("employee/index",model);
				}
				else {
					ShopOwner owner  = (ShopOwner) user;
					session.setAttribute("user", owner);
					return new ModelAndView("owner/index",model);
				}
			}
		} catch (Exception e) {
			model.addAttribute("Err", "Hệ thống đang gặp sự cố, mời đăng nhập lại hoặc sử dụng phương thức khác");
			e.printStackTrace();
			return new ModelAndView("forward:/sign",model);
		}
	}
}
