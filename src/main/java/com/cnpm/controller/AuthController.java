package com.cnpm.controller;

import com.cnpm.dto.LoginDTO;
import com.cnpm.service.impl.UserService;
import com.cnpm.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // Trả về view "login"
    }


    @GetMapping("")
    public String t(){
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            System.out.println("Người dùng hiện tại: " + userDetails.getUsername());
            return "Người dùng hiện tại: " + userDetails.getUsername();
    }

    @ResponseBody
    @RequestMapping(value = "/username", method = RequestMethod.GET)
    public String currentUserName(Principal principal) {
        return principal.getName();
    }

    @ResponseBody
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        Logger.log("Đăng nhập với tên đăng nhập: " + loginDTO.getUsername() + ", mật khẩu: " + loginDTO.getPassword());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(),
                            loginDTO.getPassword()
                    )
            );

            // Thiết lập context security
            SecurityContextHolder.getContext().setAuthentication(
                    authentication);
            // Lấy thông tin người dùng
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Lấy danh sách quyền
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            return ResponseEntity.ok("Đăng nhập thành công với quyền: " + roles);
        } catch (
                BadCredentialsException e) {
            // Xử lý đăng nhập thất bại
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai tên đăng nhập hoặc mật khẩu");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống: " + e.getMessage());
        }
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam String username, @RequestParam String password) {
        Logger.log("Đăng ký tài khoản với tên đăng nhập: " + username + ", mật khẩu: " + password);
        try {
            userService.register(username, password);
            return ResponseEntity.ok("Đăng ký tài khoản thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống: " + e.getMessage());
        }
    }
}
