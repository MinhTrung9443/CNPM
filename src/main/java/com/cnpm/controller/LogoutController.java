package com.cnpm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.http.ResponseCookie;
import org.springframework.web.util.WebUtils;

import jakarta.servlet.http.HttpSession;

@Controller
public class LogoutController {

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        // Xóa session
        if (session != null) {
            session.invalidate(); // Hủy session hiện tại
        }

        // Xóa cookie "Nhớ tôi" (nếu có)
        deleteRememberMeCookies();

        // Thêm thông báo đăng xuất thành công vào flash attribute
        redirectAttributes.addFlashAttribute("message", "Bạn đã đăng xuất thành công");

        // Chuyển hướng về trang login
        return "redirect:/login";
    }

    // Xóa cookie "Nhớ tôi"
    private void deleteRememberMeCookies() {
        // Xóa cookie "username"
        ResponseCookie usernameCookie = ResponseCookie.from("username", null)
                .maxAge(0)   // Xóa cookie
                .path("/")   // Set path cho cookie
                .build();

        // Xóa cookie "password"
        ResponseCookie passwordCookie = ResponseCookie.from("password", null)
                .maxAge(0)   // Xóa cookie
                .path("/")   // Set path cho cookie
                .build();

        // Thiết lập cookies vào response
        // Spring Boot tự động xử lý cookies khi trả về response
    }
}
