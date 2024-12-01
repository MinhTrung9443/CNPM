package com.cnpm.controller.customer;

import com.cnpm.dto.CartItemDTO;
import com.cnpm.dto.CreateOrderRequest;
import com.cnpm.dto.OrderResponse;
import com.cnpm.entity.*;
import com.cnpm.enums.PaymentMethod;
import com.cnpm.service.interfaces.IVoucherService;
import com.cnpm.service.impl.*;
import com.cnpm.service.payment.VNPAYService;
import com.cnpm.util.Logger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    ProductService productService;
    @Autowired
    PaymentService paymentService;
    @Autowired
    IVoucherService voucherService;
    @Autowired
    CartService cartService;
    @Autowired
    CartItemService cartItemService;

    @PostMapping("/preview-checkout")
    public ModelAndView checkout(@RequestParam(value = "cartItemIds", required = false) List<Long> cartItemIds, HttpSession session, ModelMap model, @RequestParam(value = "productCode", required = false) String productCode, @RequestParam(value = "quantity", required = false) Integer quantity) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return new ModelAndView("redirect:/signin");
        }

        //    example data
//        List<CartItemDTO> cartItems = List.of(
//                new CartItemDTO("Body_16", "Dầu gội thông ninh", 100000, 1, "https://static.thcdn.com/images/large/origen//productimg/1600/1600/10364465-1064965873801360.jpg"),
//                new CartItemDTO("Body_41", "Body 02", 20000, 1, "https://static.thcdn.com/images/large/origen//productimg/1600/1600/10364465-1064965873801360.jpg"),
//                new CartItemDTO("Body_63", "Dầu gội thông ninh2", 300000, 1, "https://via.placeholder.com/150")
//        );
        Logger.log("đi vào checkout");
//        Logger.log("cartItemIds: " + cartItemIds);
        List<CartItemDTO> cartItems = null;
        //nếu mua 1 sản phẩm (từ trang chi tiết sản phẩm)
        if (productCode != null) {
            Logger.log("productCode: " + productCode);
            Product product = productService.getProductByProductCode(productCode);
            // tạo ra 1 list cartItemDTO chỉ gồm 1 phần tử
            cartItems = List.of(new CartItemDTO(productCode, product.getProductName(), product.getCost(), quantity, product.getImage()));
        } else {
            //ngược lại, nếu mua nhiều sản phẩm (từ trang giỏ hàng)
            cartItems = cartItemIds.stream().map(cartItemId -> {
                CartItem cartItem = cartItemService.getCartItemById(cartItemId);
                CartItemDTO cartItemDTO = new CartItemDTO();
                cartItemDTO.setProductCode(cartItem.getProduct().getProductCode());
                cartItemDTO.setProductName(cartItem.getProduct().getProductName());
                cartItemDTO.setCost(cartItem.getProduct().getCost());
                cartItemDTO.setQuantity(cartItem.getQuantity());
                cartItemDTO.setImage(cartItem.getProduct().getImage());
                return cartItemDTO;
            }).toList();
        }
        // Lấy ra người dùng đã đăng nhập
        Long userId = ((Customer) session.getAttribute("user")).getUserId();
        double subtotal = cartItems.stream().mapToDouble(item -> item.getQuantity() * item.getCost()) // Replace 100 with the actual price of the product
                .sum();
        double total = subtotal;
        // Add calculated data to the model to be displayed in the view
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("total", total);
        model.addAttribute("userId", userId);
        return new ModelAndView("customer/checkout", model);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody @Valid CreateOrderRequest createOrderRequest, HttpSession session, HttpServletRequest request) {
        try {
            OrderResponse orderResponse = orderService.createOrder(createOrderRequest);
            Long orderId = orderResponse.getOrderId();
            String redirectUrl = null;
            // Kiểm tra phương thức thanh toán
            if (createOrderRequest.getPaymentMethod().equals(PaymentMethod.COD)) {
                orderService.updateOrderStatus(orderResponse.getOrderId());
                redirectUrl = "/order/payment-return?orderId=" + orderId;
            } else if (createOrderRequest.getPaymentMethod().equals(PaymentMethod.VNPAY)) {
                // Tạo liên kết thanh toán VNPay
                String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
                String vnpayUrl = vnpayService.createOrder(request, orderResponse.getTotal(), orderResponse.getOrderId().toString(), baseUrl);
                redirectUrl = vnpayUrl;
            } else if (createOrderRequest.getPaymentMethod().equals(PaymentMethod.PAYPAL)) {
                // Tạo liên kết thanh toán Paypal
                redirectUrl = "/paypal/checkout?orderId=" + orderId;
            } else {
                return ResponseEntity.badRequest().body("Invalid payment method");
            }
            //trả về trạng thái 200 và liên kết chuyển hướng redirectUrl
            return ResponseEntity.ok().body(Map.of("redirectUrl", redirectUrl));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/create-single-product")
    public ResponseEntity<?> createOrderForSingleProduct(@RequestBody @Valid CreateOrderRequest createOrderRequest, HttpSession session, HttpServletRequest request) {
        try {
            OrderResponse orderResponse = orderService.createOrderForSingleProduct(createOrderRequest);
            Long orderId = orderResponse.getOrderId();
            String redirectUrl = null;
            // Kiểm tra phương thức thanh toán
            if (createOrderRequest.getPaymentMethod().equals(PaymentMethod.COD)) {
                orderService.updateOrderStatus(orderResponse.getOrderId());
                redirectUrl = "/order/payment-return?orderId=" + orderId;
            } else if (createOrderRequest.getPaymentMethod().equals(PaymentMethod.VNPAY)) {
                // Tạo liên kết thanh toán VNPay
                String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
                String vnpayUrl = vnpayService.createOrder(request, orderResponse.getTotal(), orderResponse.getOrderId().toString(), baseUrl);
                redirectUrl = vnpayUrl;
            } else if (createOrderRequest.getPaymentMethod().equals(PaymentMethod.PAYPAL)) {
                // Tạo liên kết thanh toán Paypal
                redirectUrl = "/paypal/checkout?orderId=" + orderId;
            } else {
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
    public String paymentProcess(@RequestParam("orderId") Long orderId, Model model, HttpSession session) {
        // Xử lý thanh toán
        Order order = orderService.getOrderById(orderId);
        //Lấy ra người dùng đã đăng nhập
        Long userId = ((Customer) session.getAttribute("user")).getUserId();
        // Kiểm tra xem đơn hàng có tồn tại và có thuộc về người dùng đó không, nếu không trả về trang lỗi
        if (order == null || !order.getCustomerId().equals(userId)) {
            return "err/error";
        }
        model.addAttribute("order", order);
        return "customer/ordersuccess";

    }

    @GetMapping("/reprocessPayment/{orderId}")
    public String reprocessPayment(@PathVariable Long orderId, HttpSession session, Model model,HttpServletRequest request) {
        Order order = orderService.getOrderById(orderId);
        //Lấy ra người dùng đã đăng nhập
        Long userId = ((Customer) session.getAttribute("user")).getUserId();
        // Kiểm tra xem đơn hàng có tồn tại và có thuộc về người dùng đó không, nếu không trả về trang lỗi
        if (order == null || !order.getCustomerId().equals(userId)) {
            return "err/error";
        }
        //lấy ra phuog thức thanh toán của đơn hàng
        PaymentMethod paymentMethod = orderService.getPaymentMethodByOrderId(orderId);
        String redirectUrl = null;
        if (paymentMethod.equals(PaymentMethod.VNPAY)) {
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String vnpayUrl = vnpayService.createOrder(request, order.getTotal().intValue(), order.getOrderId().toString(), baseUrl);
            redirectUrl = vnpayUrl;
        } else if (paymentMethod.equals(PaymentMethod.PAYPAL)) {
            redirectUrl = "/paypal/checkout?orderId=" + orderId;
        }
        return "redirect:" + redirectUrl;
    }

}
