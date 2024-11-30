package com.cnpm.controller.customer;

import com.cnpm.dto.CartItemDTO;
import com.cnpm.dto.CreateOrderRequest;
import com.cnpm.dto.OrderResponse;
import com.cnpm.entity.*;
import com.cnpm.enums.PaymentMethod;
import com.cnpm.service.IVoucherService;
import com.cnpm.service.impl.*;
import com.cnpm.service.vnpay.VNPAYService;
import com.cnpm.util.Logger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Payload;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    VNPAYService vnpayService;
    @Autowired
    PaymentService paymentService;
    @Autowired
    IVoucherService voucherService;
    @Autowired
    CartService cartService;
    @Autowired
    CartItemService cartItemService;

//    @GetMapping("/preview-checkout2")
//    @ResponseBody
//    public String checkout2(HttpSession session, ModelMap model, String voucherCode) {
//        Logger.log("user:" + session.getAttribute("user"));
//        Logger.log("username:" + session.getAttribute("username"));
//        return "Hello";
//    }


    @PostMapping("/preview-checkout")
    public ModelAndView checkout(@RequestParam("cartItemIds") List<Long> cartItemIds, HttpSession session, ModelMap model) {
//        Logger.log("" +session.getAttribute("user"));
//        Logger.log("" +session.getAttribute("username"));

        //    example data
//        List<CartItemDTO> cartItems = List.of(
//                new CartItemDTO("Body_16", "Dầu gội thông ninh", 100000, 1, "https://static.thcdn.com/images/large/origen//productimg/1600/1600/10364465-1064965873801360.jpg"),
//                new CartItemDTO("Body_41", "Body 02", 20000, 1, "https://static.thcdn.com/images/large/origen//productimg/1600/1600/10364465-1064965873801360.jpg"),
//                new CartItemDTO("Body_63", "Dầu gội thông ninh2", 300000, 1, "https://via.placeholder.com/150")
//        );
//        Logger.log("cartItemIds: " + cartItemIds);
        Long userId= ((Customer) session.getAttribute("user")).getUserId();
        Cart cart = cartService.getCartByUserId(userId);
        //anh xa toan bo cart thanh cartItemDTO
//        List<CartItemDTO> cartItems = cart.getCartItems().stream().map(cartItem -> {
//            CartItemDTO cartItemDTO = new CartItemDTO();
//            cartItemDTO.setProductCode(cartItem.getProduct().getProductCode());
//            cartItemDTO.setProductName(cartItem.getProduct().getProductName());
//            cartItemDTO.setCost(cartItem.getProduct().getCost());
//            cartItemDTO.setQuantity(cartItem.getQuantity());
//            cartItemDTO.setImage(cartItem.getProduct().getImage());
//            return cartItemDTO;
//        }).toList();
        //anh xa tung cartiemid thanh cartItemDTO
        List<CartItemDTO> cartItems = cartItemIds.stream().map(cartItemId -> {
            CartItem cartItem = cartItemService.getCartItemById(cartItemId);
            CartItemDTO cartItemDTO = new CartItemDTO();
            cartItemDTO.setProductCode(cartItem.getProduct().getProductCode());
            cartItemDTO.setProductName(cartItem.getProduct().getProductName());
            cartItemDTO.setCost(cartItem.getProduct().getCost());
            cartItemDTO.setQuantity(cartItem.getQuantity());
            cartItemDTO.setImage(cartItem.getProduct().getImage());
            return cartItemDTO;
        }).toList();

        double subtotal = cartItems.stream().mapToDouble(item -> item.getQuantity() * item.getCost()) // Replace 100 with the actual price of the product
                .sum();
        double total = subtotal; // You can include discount logic here if needed
        Double discount = 0.0;
//        if (voucherCode != null) {
//            Logger.log("Voucher code: " + voucherCode);
//            Voucher voucher = voucherService.findFirst(voucherCode).orElse(null);
//            if (voucher == null) {
//                model.addAttribute("error", "Voucher " + voucherCode + " is not available");
//            } else
//                discount = voucherService.getDiscount(voucherCode);
//            Logger.log("Discount: " + discount);
//            total = total - discount;
//        }

        // Add calculated data to the model to be displayed in the view
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("total", total);
//        model.addAttribute("voucherCode", voucherCode);
        model.addAttribute("userId", userId);
//        return "customer/checkout";
        return new ModelAndView("customer/checkout", model);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody @Valid CreateOrderRequest createOrderRequest, HttpSession session, HttpServletRequest request) {
        try {
            OrderResponse orderResponse = orderService.createOrder(createOrderRequest);
            Long orderId = orderResponse.getOrderId();
            String redirectUrl=null;
            // Kiểm tra phương thức thanh toán
            if (createOrderRequest.getPaymentMethod().equals(PaymentMethod.COD)) {
                orderService.updateOrderStatus(orderResponse.getOrderId());
                redirectUrl ="/order/payment-return?orderId=" + orderId;
            } else if (createOrderRequest.getPaymentMethod().equals(PaymentMethod.VNPAY)) {
                // Tạo liên kết thanh toán VNPay
                String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
                String vnpayUrl = vnpayService.createOrder(request, orderResponse.getTotal(), orderResponse.getOrderId().toString(), baseUrl);
                redirectUrl = vnpayUrl;
            } else if (createOrderRequest.getPaymentMethod().equals(PaymentMethod.PAYPAL)) {
                // Tạo liên kết thanh toán Paypal
                redirectUrl = "/paypal/checkout?orderId=" + orderId;
            }
            else{
                return ResponseEntity.badRequest().body("Invalid payment method");
            }
            //trả về trạng thái 200 và liên kết chuyển hướng redirectUrl
            return ResponseEntity.ok().body(Map.of("redirectUrl", redirectUrl));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Controller này xử lý cho phương thức thanh toán COD
    @GetMapping("/payment-return")
    public String paymentProcess(@RequestParam("orderId") Long orderId, Model model,HttpSession session){
        // Xử lý thanh toán
        Order order = orderService.getOrderById(orderId);
        //Lấy ra người dùng đã đăng nhập
        Long userId = ((Customer)session.getAttribute("user")).getUserId();
        // Kiểm tra xem đơn hàng có tồn tại và có thuộc về người dùng đó không, nếu không trả về trang lỗi
        if (order == null || !order.getCustomerId().equals(userId)) {
            return "err/error";
        }
        model.addAttribute("order", order);
        return "customer/ordersuccess";

    }

}
