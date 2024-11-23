package com.cnpm.controller;

import com.cnpm.dto.ProductHistoryDTO;
import com.cnpm.dto.PurchaseHistoryDTO;
import com.cnpm.entity.Order;
import com.cnpm.entity.User;
import com.cnpm.enums.OrderStatus;
import com.cnpm.service.impl.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Set;

@Controller
@RequestMapping("/customer/order-history")
public class OrderHistoryController {
    private final OrderService orderService;

    public OrderHistoryController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("")
    public String getOrderHistory(
            @RequestParam(value = "tab", required = false, defaultValue = "tat-ca-don-hang") String tab,
            Model model, HttpSession session) {

        // Lấy thông tin người dùng từ session
//        User user = (User) session.getAttribute("user");
//        if (user == null) {
//            return "redirect:user/signin"; // Nếu người dùng chưa đăng nhập, chuyển hướng đến trang đăng nhập
//        }
//
//        Long customerId = user.getUserId();
        Set<PurchaseHistoryDTO> orders;
        Long customerId = 21L;

        // Ánh xạ trực tiếp từ "tab" sang trạng thái đơn hàng
        switch (tab) {
            case "tat-ca-don-hang":
                orders = orderService.getAllOrders(customerId); // Lấy tất cả đơn hàng của người dùng
                break;
            case "don-cho-xac-nhan":
                orders = orderService.getPurchaseHistory(customerId, OrderStatus.PENDING); // Đơn chờ xác nhận
                break;
            case "don-da-xac-nhan":
                orders = orderService.getPurchaseHistory(customerId, OrderStatus.CONFIRMED); // Đơn đã xác nhận
                break;
            case "don-dang-van-chuyen":
                orders = orderService.getPurchaseHistory(customerId, OrderStatus.SHIPPING); // Đơn đang vận chuyển
                break;
            case "don-da-giao":
                orders = orderService.getPurchaseHistory(customerId, OrderStatus.COMPLETED); // Đơn đã giao
                break;
            case "don-huy":
                orders = orderService.getPurchaseHistory(customerId, OrderStatus.CANCELLED);
                orders.addAll(orderService.getPurchaseHistory(customerId, OrderStatus.REFUNDED));
                break;
            default:
                orders = orderService.getAllOrders(customerId); // Mặc định là tất cả đơn hàng của người dùng
                break;
        }

        // Gửi thông tin đến view
        model.addAttribute("orders", orders);
        model.addAttribute("tab", tab);
        return "customer/order-history";
    }
}
