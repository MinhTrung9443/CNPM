package com.cnpm.controller;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import com.cnpm.entity.Product;
import com.cnpm.entity.Role;
import com.cnpm.entity.ShopOwner;
import com.cnpm.entity.User;
import com.cnpm.service.IAccountService;
import com.cnpm.service.IProductService;
import com.cnpm.service.IUserService;
import com.cnpm.service.capcha.CaptchaService;
import com.cnpm.service.mail.MailService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping({ "", "*/" })
public class AuthController {
	@Autowired
	IUserService userService;
	@Qualifier("accountService")
	@Autowired
	IAccountService accService;
	@Autowired
	MailService mailservice;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private IProductService productService;
    @Autowired
    private CaptchaService captchaService;
	@GetMapping("/signup")
	String signup(Model model, HttpSession session) {

		session = request.getSession(false);
		String url = checkSignIn(session);
		if (!url.isEmpty()) {
			return url;
		}
		Customer cus = new Customer();
		model.addAttribute(cus);
		return "user/signup";
	}

	@GetMapping("/signin")
	public String signin(Model model, HttpSession session) {
		String url = checkSignIn(session);
		if (!url.isEmpty()) {
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
		List<Product> allProducts = productService.findAllDistinctProduct();
		model.addAttribute("products", allProducts);
		return "redirect:/index";
	}

	@PostMapping("/signup")
	public ModelAndView SignUp(ModelMap model, HttpSession session, @Valid @ModelAttribute Customer customer,
			@Valid @ModelAttribute Account account, @RequestParam String confirmPassword, BindingResult result) {
		if (result.hasErrors()) {
			model.addAttribute("customer", customer);
			return new ModelAndView("user/signup", model);
		}

		boolean check = false;

		if (userService.findByEmail(customer.getEmail()) != null) {
			model.addAttribute("emailExit", "Email đã tồn tại");
			check = true;
		}

		if (userService.findByPhone(customer.getPhone()) != null) {
			model.addAttribute("phoneExit", "Số điện thoại đã tồn tại");
			check = true;
		}

		if (userService.findByUsername(account.getUsername()) != null) {
			model.addAttribute("usernameExit", "Tên đăng nhập đã tồn tại");
			check = true;
		}

		if (!confirmPassword.equals(account.getPassword())) {
			model.addAttribute("Confirmpass", "Mật khẩu không khớp");
			check = true;
		}

		String phoneRegex = "^\\d{10}$";
		if (!Pattern.matches(phoneRegex, customer.getPhone())) {
			model.addAttribute("phoneErr", "Số điện thoại không hợp lệ");
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
		role.setRoleName("customer");

		BeanUtils.copyProperties(customer, cus);
		BeanUtils.copyProperties(account, acc);

		acc.setRole(role);
		acc.setUser(cus);
		cus.setAccount(acc);

		userService.save(cus);
		Customer newCustomer = (Customer) userService.findByEmail(cus.getEmail());

		session.setAttribute("user", newCustomer);
		return new ModelAndView("redirect:/index", model);
	}

    @PostMapping("/signin")
    public ModelAndView signin( ModelMap model, HttpSession session, @RequestParam String username,
            @RequestParam String password) {
        
        Account account = userService.findByUsernameAndPassword(username, password);
        if (account == null) {
			// Nếu không tìm thấy tài khoản, thử kiểm tra nếu đăng nhập qua Google hoặc
			// Facebook
			String provider = (String) session.getAttribute("provider"); // Kiểm tra xem có provider không (GG hoặc FB)
			if (provider != null) {
				// Đăng nhập qua Google hoặc Facebook, gán fullname là "guest"
				session.setAttribute("fullname", "guest");
				model.addAttribute("Err", "Đăng nhập thành công với tài khoản Google/Facebook");
				return new ModelAndView("redirect:/index", model);
			} else {
				model.addAttribute("Err", "Tên đăng nhập hoặc mật khẩu không đúng");
				return new ModelAndView("user/signin", model);
			}
        }

		int roleid = account.getRole().getRoleId().intValue();
		if (roleid == 3 && account.getUser() instanceof Customer) {
			Customer customer = (Customer) account.getUser();
			session.setAttribute("user", customer);
			session.setAttribute("fullname", customer.getFullName()); // Lấy fullname từ Customer
			return new ModelAndView("redirect:/index", model);
		} else if (roleid == 2 && account.getUser() instanceof Employee) {
			Employee employee = (Employee) account.getUser();
			session.setAttribute("user", employee);
			session.setAttribute("fullname", employee.getFullName()); // Lấy fullname từ Employee
			return new ModelAndView("employee/index", model);
		} else if (roleid == 1 && account.getUser() instanceof ShopOwner) {
			ShopOwner owner = (ShopOwner) account.getUser();
			session.setAttribute("user", owner);
			return new ModelAndView("owner/index", model);
		} else {
			model.addAttribute("Err", "Invalid user role or user type.");
			return new ModelAndView("user/signin", model);
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
			e.printStackTrace(); // TODO: handle exception
		}
		model.addAttribute("Err", "Email không tồn tại, mời nhập lại thông tin");
		return new ModelAndView("user/forgotPass", model);
	}

	@PostMapping("/resetpassword")
	public ModelAndView resetPassword(ModelMap model, HttpSession session, @RequestParam String otp,
			@RequestParam String newPassword) {
		String OTP = session.getAttribute("OTP").toString();
		User user = (User) session.getAttribute("userSetpass");

		if (otp.equals(OTP)) {
			user.getAccount().setPassword(newPassword);
			accService.save(user.getAccount());
			return new ModelAndView("redirect:/signin", model);
		}
		model.addAttribute("OtpErr", "Mã OTP không trùng khớp");
		return new ModelAndView("forward:user/AuthOTP", model);
	}

	private String checkSignIn(HttpSession session) {

		User user = (User) session.getAttribute("user");
		if (user != null) {
			long roleid = user.getAccount().getRole().getRoleId();
			if (roleid == 3) {
				return "redirect:/customer/index";
			} else if (roleid == 2) {
				return "redirect:/employee/index.html";
			} else {
				return "redirect:/owner/index";
			}
		}
		return "";
	}
}
