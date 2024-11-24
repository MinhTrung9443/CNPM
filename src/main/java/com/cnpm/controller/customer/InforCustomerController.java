package com.cnpm.controller.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cnpm.entity.Customer;
import com.cnpm.entity.User;
import com.cnpm.service.IUserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/customer")
public class InforCustomerController {
	@Autowired
    IUserService userService;

    @GetMapping("/profile")
    public String viewUserProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/signin";
        }
        if (user instanceof Customer) {
            Customer customer = (Customer) user; // Chuyển kiểu xuống Customer
            model.addAttribute("birthDate", customer.getBirthDate());
        }

        model.addAttribute("user", user);

        return "customer/profile";
    }

    
    @GetMapping("/update")
    public String editUserProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/signin"; 
        }
        if (user instanceof Customer) {
            Customer customer = (Customer) user; // Chuyển kiểu xuống Customer
            model.addAttribute("user", customer); 
        }
 
        model.addAttribute("user", user);

        return "customer/updateProfile"; 
    }

    @PostMapping("/update")
    public String updateUserProfile(HttpSession session, Model model, @ModelAttribute Customer updatedUser) {
        Customer user = (Customer) userService.findById(updatedUser.getUserId()).get();
        if (user == null) {
            return "redirect:/signin"; 
        }

        User existingUser = userService.findByEmail(updatedUser.getEmail());
        if (existingUser != null && !existingUser.getUserId().equals(updatedUser.getUserId())) {
            // Nếu email đã tồn tại và không phải của người dùng hiện tại
            model.addAttribute("error", "Email đã được sử dụng, vui lòng nhập email khác.");
            model.addAttribute("user", updatedUser);
            return "customer/updateProfile"; // Quay lại trang chỉnh sửa
        }
        
        user.setBirthDate(updatedUser.getBirthDate());
        user.setFullName(updatedUser.getFullName());
        user.setEmail(updatedUser.getEmail());
        user.setPhone(updatedUser.getPhone());
        user.setAddress(updatedUser.getAddress());
        user.setGender(updatedUser.getGender());

        userService.save(user);

        session.setAttribute("user", user);

        model.addAttribute("user", user);

        return "/customer/profile"; 
    }



}
