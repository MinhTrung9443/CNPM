package com.cnpm.service.capcha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class RegistrationController {

    @Autowired
    private CaptchaService captchaService;

    @PostMapping("/registration")
    @ResponseBody
    public ResponseEntity<?> registerUser(@RequestParam("g-recaptcha-response") String response) {
        captchaService.processResponse(response);

        // Xử lý logic đăng ký người dùng ở đây
        return ResponseEntity.ok("User registered successfully!");
    }
    @GetMapping("/registration")
    public String registerUser() {

        // Xử lý logic đăng ký người dùng ở đây
        return "dk";
    }
}
