package com.cnpm.controller.Payment;

import com.cnpm.entity.Customer;
import com.cnpm.service.IOrderService;
import com.cnpm.service.impl.OrderService;
import com.cnpm.service.paypal.PaypalService;
import com.cnpm.util.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.sdk.Environment;
import com.paypal.sdk.PaypalServerSdkClient;
import com.paypal.sdk.authentication.ClientCredentialsAuthModel;
import com.paypal.sdk.controllers.OrdersController;
import com.paypal.sdk.exceptions.ApiException;
import com.paypal.sdk.http.response.ApiResponse;
import com.paypal.sdk.models.AmountWithBreakdown;
import com.paypal.sdk.models.CheckoutPaymentIntent;
import com.paypal.sdk.models.Order;
import com.paypal.sdk.models.OrderRequest;
import com.paypal.sdk.models.OrdersCaptureInput;
import com.paypal.sdk.models.OrdersCreateInput;
import com.paypal.sdk.models.PurchaseUnitRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;

// 4032031934855593
// 05/2026
// 760
@Controller
@RequestMapping("/paypal")
public class PaypalController {
    @Autowired
    @Lazy
    PaypalService paypalService;

    @Autowired
    IOrderService orderService;
    //    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/checkout")
    public ModelAndView checkout(@RequestParam("orderId") Long orderId,HttpSession session, Model model) {
        orderService.getOrderById(orderId);
        //Lấy ra người dùng đã đăng nhập
        Long userId = ((Customer)session.getAttribute("user")).getUserId();
        // Kiểm tra xem đơn hàng có thuộc về người dùng đó không, nếu không trả về trang lỗi
        if (!orderService.getOrderById(orderId).getCustomerId().equals(userId)) {
            return new ModelAndView("err/500");
        }
        model.addAttribute("orderId", orderId);
        return new ModelAndView("customer/paypalcheckout");
    }

    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(@RequestBody Map<String, Object> request, HttpSession session
//            ,
//                                             @RequestParam("amount") Double orderTotal,
//                                             @RequestParam("orderInfo") String orderInfo
    ) {
        try {
            String cart = objectMapper.writeValueAsString(request.get("cart"));
            Long orderId=objectMapper.convertValue(request.get("orderId"), Long.class);
            // Tương tự, kiêm tra orderId có thuộc về người dùng hiện tại không
            Long userId = ((Customer)session.getAttribute("user")).getUserId();
            if (!orderService.getOrderById(Long.parseLong(orderId.toString())).getCustomerId().equals(userId)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            Double orderTotal = orderService.getOrderTotal(orderId);
            Order response = paypalService.createOrder(cart, orderTotal);
            //lưu paypayOrderId vào redis, key là paypalOrderId, value là orderId của hệ thống
            session.setAttribute("paypalOrderId:"+response.getId(), orderId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{orderID}/capture")
    public ResponseEntity<?> captureOrder(@PathVariable String orderID, HttpSession session) {
        try {
            //Lấy orderId từ session
            Long orderId = Long.parseLong(session.getAttribute("paypalOrderId:" + orderID).toString());
            // Kiểm tra xem orderId có thuộc về người dùng hiện tại không
            Long userId = ((Customer)session.getAttribute("user")).getUserId();
            if (!orderService.getOrderById(orderId).getCustomerId().equals(userId)) {
                return new ResponseEntity<>("Đơn hàng không thuộc về bạn", HttpStatus.BAD_REQUEST);
            }
            Order response = paypalService.captureOrders(orderID);
//            Logger.log("capture" + response);
//            orderService.updateOrderStatus(orderID, "COMPLETED");
//            Logger.log("status" + response.getStatus().toString());

            if (response.getStatus().toString().equals("COMPLETED")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                // Chuyển đổi sang String
                String formattedDate = LocalDateTime.now().format(formatter);
                orderService.updateOrderStatusPaymentTime(orderId, formattedDate);
            }
            return new ResponseEntity<Order>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//        private Order createOrder(String cart, Double amount) throws IOException, ApiException {
//
//            OrdersCreateInput ordersCreateInput = new OrdersCreateInput.Builder(
//                    null,
//                    new OrderRequest.Builder(
//                            CheckoutPaymentIntent.CAPTURE,
//                            Arrays.asList(
//                                    new PurchaseUnitRequest.Builder(
//                                            new AmountWithBreakdown.Builder(
//                                                    "USD",
////													"999.00",
//                                                    String.valueOf(0.000039*amount)
//                                            )
//                                                    .build())
//                                            .build()))
//                            .build())
//                    .build();
//
//            OrdersController ordersController = client.getOrdersController();
//
//            ApiResponse<Order> apiResponse = ordersController.ordersCreate(ordersCreateInput);
//
//            return apiResponse.getResult();
//        }
//
//        private Order captureOrders(String orderID) throws IOException, ApiException {
//            OrdersCaptureInput ordersCaptureInput = new OrdersCaptureInput.Builder(
//                    orderID,
//                    null)
//                    .build();
//            OrdersController ordersController = client.getOrdersController();
//            ApiResponse<Order> apiResponse = ordersController.ordersCapture(ordersCaptureInput);
//            return apiResponse.getResult();
//        }
//
}