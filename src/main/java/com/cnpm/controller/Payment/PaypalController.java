package com.cnpm.controller.Payment;

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
    public ModelAndView checkout(
//            @RequestParam("orderId") Long orderId,
            HttpSession session, Model model) {
        Logger.log("checkout paypal" + session.getAttribute("orderId"));
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
            Long orderId = Long.parseLong(session.getAttribute("orderId").toString());
//            Logger.log(cart);
            Double orderTotal = orderService.getOrderTotal(orderId);
            Order response = paypalService.createOrder(cart, orderTotal);
//            System.out.println(response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{orderID}/capture")
    public ResponseEntity<Order> captureOrder(@PathVariable String orderID, HttpSession session) {
        try {
            Order response = paypalService.captureOrders(orderID);
            Logger.log("capture" + response);
//            orderService.updateOrderStatus(orderID, "COMPLETED");
            Logger.log("status" + response.getStatus().toString());

            if (response.getStatus().toString().equals("COMPLETED")) {
//                return new ResponseEntity<Order>(response, HttpStatus.OK);
                Long orderId = session.getAttribute("orderId") != null ? (Long) session.getAttribute("orderId") : null;
                if (orderId == null) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                Logger.log("orderId" + orderId);
                Logger.log("formattedDate" + LocalDateTime.now().format(formatter));
                // Chuyển đổi sang String
                String formattedDate = LocalDateTime.now().format(formatter);
                orderService.updateOrderStatus(orderId, formattedDate);

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