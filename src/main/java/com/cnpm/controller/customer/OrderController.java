package com.cnpm.controller.customer;

import com.cnpm.entity.User;
import com.cnpm.service.IOrderService;
import com.cnpm.service.IUserService;
import com.cnpm.service.impl.OrderService;
import com.cnpm.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IUserService userService;

    @GetMapping("/create")
    public String createOrder(Principal principal) {
        Logger.log(principal.getName()); // principal.getName() trả về username của user đăng nhập-> lấy đc user
        User user = userService.getUser(principal.getName());
        Logger.log(user);
        return "order/create";
    }
}
