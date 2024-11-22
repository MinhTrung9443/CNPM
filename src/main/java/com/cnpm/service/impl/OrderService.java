package com.cnpm.service.impl;

import com.cnpm.dto.CartItemForOrderDTO;
import com.cnpm.dto.CreateOrderRequest;
import com.cnpm.dto.OrderResponse;
import com.cnpm.entity.*;
import com.cnpm.enums.OrderStatus;
import com.cnpm.enums.PaymentMethod;
import com.cnpm.enums.PaymentStatus;
import com.cnpm.repository.*;
import com.cnpm.service.IOrderService;
import com.cnpm.service.vnpay.VNPAYService;
import com.cnpm.util.Logger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    VoucherRepository voucherRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    VNPAYService vnPayService;

    @Transactional
    public OrderResponse createOrder(@Valid CreateOrderRequest createOrderRequest) {
        // TODO: chưa xử lý trường hợp tranh nhau đặt hàng
        // Tìm người dùng
//        Logger.log("Create order request: " + createOrderRequest);
        User user = userRepository.findById(createOrderRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Khởi tạo đơn hàng và các dòng đơn hàng
        Order order = new Order();
        Set<CartItemForOrderDTO> cartItemForOrderDTOS = createOrderRequest.getCartItemForOrderDTOS();
        Set<OrderLine> orderLines = new LinkedHashSet<>();
        // Lấy danh sách mã sản phẩm và tạo map các sản phẩm
//        Set<String> productCodes = cartItemForOrderDTOS.stream()
//                .map(CartItemForOrderDTO::getProductCode)
//                .collect(Collectors.toSet());
//        Map<String, Product> productMap = productRepository.findDistinctByProductCodeIn(productCodes).stream()
//                .collect(Collectors.toMap(Product::getProductCode, Function.identity()));

        // Lặp qua các mặt hàng trong giỏ hàng và tạo OrderLine
        double total = 0.0;
        for (var cartItem : cartItemForOrderDTOS) {

            Product product = productRepository.findFirstByProductCode(cartItem.getProductCode())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            //check quantity
            Logger.log("Product: " + product);
            Logger.log("Product service: " + productService.getStockByProductCode(product.getProductCode()));
            if (productService.getStockByProductCode(product.getProductCode()) < cartItem.getQuantity()) {
                throw new RuntimeException("Product " + product.getProductCode() + " out of stock");
            }
//          đánh dấu sản phẩm đã được mua, số sp được đánh dấu là số lượng mua
            for (int i = 0; i < cartItem.getQuantity(); i++) {
//                tìm và đánh dấu sản phẩm đã được mua
                Product product1 = productRepository.findFirstByProductCode(product.getProductCode())
                        .orElseThrow(() -> new RuntimeException("Product not found"));
                product1.setIsUsed(true);
                productRepository.save(product1);
            }
            OrderLine orderLine = new OrderLine();
            orderLine.setProduct(product);
            orderLine.setQuantity(Math.toIntExact(cartItem.getQuantity()));
            orderLine.setOrder(order);
            orderLines.add(orderLine);
            Logger.log("Order line: " + orderLine);
            // Cộng dồn tổng giá trị
            total += product.getCost() * cartItem.getQuantity();
        }
//        apply discount
//        Logger.log("Order line: " + orderLines);
        if (createOrderRequest.getVoucherCodes() != null) {
            for (String voucherCode : createOrderRequest.getVoucherCodes()) {
                Voucher voucher = voucherRepository.findFirstByVoucherCodeAndIsUsedFalseAndStartDateBeforeAndEndDateAfter(voucherCode, LocalDateTime.now(), LocalDateTime.now())
                        .orElseThrow(() -> new RuntimeException("Voucher " + voucherCode + " is not available"));
                voucher.setIsUsed(true);
                voucherRepository.save(voucher);
                total -= voucher.getVoucherValue();
            }
        }
        // Thiết lập các thuộc tính cho đơn hàng
        order.setOrderLines(orderLines);
        order.setTotal(total<0?0:total);
        order.setCustomerId(user.getUserId());
        order.setOrderDate(LocalDateTime.ofInstant(new Date().toInstant(), TimeZone.getDefault().toZoneId()));
        order.setOrderStatus(OrderStatus.PENDING);
        order.setShippingAddress(createOrderRequest.getAddress());

        // Lưu đơn hàng và tạo phản hồi
        Order savedOrder = orderRepository.save(order);



        // Lưu Payment và thiết lập phương thức thanh toán cho phản hồi
        Payment payment = new Payment();
        payment.setOrder(savedOrder);
        payment.setTotal(total);
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setPaymentMethod(createOrderRequest.getPaymentMethod());
        paymentRepository.save(payment);

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId(savedOrder.getOrderId());
        orderResponse.setTotal(savedOrder.getTotal().intValue());
        orderResponse.setPaymentMethod(payment.getPaymentMethod());
        return orderResponse;
    }

    public void updateOrderStatus(Long orderId, String paymentTime) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setOrderStatus(OrderStatus.CONFIRMED);
        Payment payment = paymentRepository.findByOrder(order)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setPaymentStatus(PaymentStatus.PAID);
        payment.setPaymentDate(LocalDateTime.parse(paymentTime, DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        paymentRepository.save(payment);
        orderRepository.save(order);
    }

    public void updateOrderStatus(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setOrderStatus(OrderStatus.CONFIRMED);
        Payment payment = paymentRepository.findByOrder(order)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setPaymentDate(order.getOrderDate());
        paymentRepository.save(payment);
        orderRepository.save(order);
    }
}
