package com.cnpm.controller;

import java.util.regex.Pattern;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cnpm.dto.mail.MailDto;
import com.cnpm.entity.Account;
import com.cnpm.entity.Customer;
import com.cnpm.entity.Employee;
import com.cnpm.entity.Role;
import com.cnpm.entity.ShopOwner;
import com.cnpm.entity.User;
import com.cnpm.service.IAccountService;
import com.cnpm.service.IUserService;
import com.cnpm.service.mail.MailService;

import io.netty.util.internal.ThreadLocalRandom;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("")
public class AuthController {
	@Autowired
	IUserService userService;
	@Autowired
	IAccountService accService;
	@Autowired
	MailService mailservice;
	@Autowired
	private HttpServletRequest request;
	
	@GetMapping("/signup")
	String signup(Model model, HttpSession session) {
		session = request.getSession(false);
		String url = checkSignIn(session);
		if (url != "")
		{
			return url;
		}
		Customer cus = new Customer();
		model.addAttribute(cus);
		return "user/signup";
	}

	@GetMapping("/signin")
	String signin(Model model, HttpSession session) {
		session = request.getSession(false);
		String url = checkSignIn(session);
		if (url != "")
		{
			return url;
		}
		return "user/signin";
	}

	@GetMapping("/forgotpass")
	public String forgotPass() {
		return "user/forgotPass";
	}

	@GetMapping("/signout")
	public String signout(Model model, HttpSession session) {
		session.invalidate();
		return "customer/index";
	}

	@PostMapping("/signup")
	public ModelAndView SignUp(ModelMap model, HttpSession session, @Valid @ModelAttribute Customer customer,
			@Valid @ModelAttribute Account account, @RequestParam String confirmPassword, BindingResult result) {
		if (result.hasErrors()) {
			model.addAttribute("customer", customer);
			return new ModelAndView("/signup", model);
		}

		boolean check = false;

		if (!confirmPassword.equals(account.getPassword())) {
			model.addAttribute("Confirmpass", "Mật khẩu không khớp");
			check = true;
		}

		if (userService.findByEmail(customer.getEmail()) != null) {
			model.addAttribute("emailExit", "Email đã tồn tại");
			check = true;
		}

		if (userService.findByPhone(customer.getPhone()) != null) {
			model.addAttribute("phoneExit", "Số điện thoại đã tồn tại");
			check = true;
		}
		String phoneRegex = "^\\d{10}$";
		if (!Pattern.matches(phoneRegex, customer.getPhone())) {
			model.addAttribute("phoneErr", "Số điện thoại không hợp lệ");
			check = true;
		}
		if (userService.findByUsername(account.getUsername()) != null) {
			model.addAttribute("usernameExit", "Tên đăng nhập đã tồn tại");
			check = true;
		}
		if (check == true) {
			model.addAttribute("customer", customer);
			return new ModelAndView("user/signup", model);
		}

		Customer cus = new Customer();
		Account acc = new Account();
		Role role = new Role();
		role.setRoleId((long) 3);
		role.setRoleName("user");

		BeanUtils.copyProperties(customer, cus);
		BeanUtils.copyProperties(account, acc);

		acc.setRole(role);
		acc.setUser(cus);
		cus.setAccount(acc);

		System.out.println(acc.toString() + " " + cus.toString());

		userService.save(cus);
		session.setAttribute("account", acc);
		return new ModelAndView("redirect:/index", model);
	}

	@PostMapping("/signin")
	public ModelAndView signin(ModelMap model, HttpSession session, @RequestParam String username,
			@RequestParam String password) {

		Account account = userService.findByUsernameAndPassword(username, password);
		if (account == null) {
			model.addAttribute("Err", "Ten dang nhap hoac mat khau khong dung");
			return new ModelAndView("user/signin", model);
		}
		int roleid = account.getRole().getRoleId().intValue();
		if (roleid == 3) {
			Customer customer = (Customer) account.getUser();
			session.setAttribute("user", customer);
			return new ModelAndView("customer/index", model);
		} else if (roleid == 2) {
			Employee employee = (Employee) account.getUser();
			session.setAttribute("user", employee);
			return new ModelAndView("employee/index", model);
		} else {
			ShopOwner owner = (ShopOwner) account.getUser();
			session.setAttribute("user", owner);
			return new ModelAndView("owner/index", model);
		}
	}

	@PostMapping("/forgotpass")
	public ModelAndView forgotpass(ModelMap model, HttpSession session, @RequestParam String email) {
		try {
			User user = userService.findByEmail(email);
			if (user != null) {
				StringBuilder randomNumber = new StringBuilder();

				for (int i = 0; i < 5; i++) {
					randomNumber.append(ThreadLocalRandom.current().nextInt(10));
				}
				String OTP = randomNumber.toString();
				String content = "Mã OTP: " + OTP
						+ "\n Lưu ý: Mã OTP tồn tại trong 10p. Quá 10p Mời thực hiện lại quy trình đổi mật khẩu!!!!";
				MailDto mail = new MailDto();
				mail.setEmail(user.getEmail());
				mail.setDescription(content);
				mail.setName("OTP");
				mailservice.sendEmail(mail);
				session.setAttribute("userSetpass", user);
				session.setAttribute("OTP", OTP);
				session.setMaxInactiveInterval(10 * 60);
				return new ModelAndView("user/AuthOTP", model);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("customer/index", model);

	}

	@PostMapping("/resetpassword")
	public ModelAndView resetPassword(ModelMap model, HttpSession session, @RequestParam String otp,
			@RequestParam String newPassword) {
		String OTP = session.getAttribute("OTP").toString();
		User user = (User) session.getAttribute("userSetpass");
		if (user == null) {
			System.out.println("null");
		}
		if (otp.equals(OTP)) {
			user.getAccount().setPassword(newPassword);
			accService.save(user.getAccount());
			return new ModelAndView("customer/index", model);
		}
		model.addAttribute("OtpErr", "Mã OTP không trùng khớp");
		return new ModelAndView("forward:user/AuthOTP", model);
	}

	private String checkSignIn(HttpSession session) {
		
		User user = (User) session.getAttribute("user");
		if (user != null) {
			int roleid = user.getAccount().getRole().getRoleId().intValue();
			if (roleid == 3) {
				return "customer/index";
			} else if (roleid == 2) {
				return "employee/index";
			} else {
				return "owner/index";
			}
		}
		return "";
	}
}
