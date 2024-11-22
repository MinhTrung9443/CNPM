package com.cnpm.service.impl;

import com.cnpm.dto.OrderDetailEmployeeDTO;
import com.cnpm.dto.OrderLineDTO;
import com.cnpm.entity.User;
import com.cnpm.entity.Order;
import com.cnpm.entity.OrderLine;
import com.cnpm.entity.Payment;
import com.cnpm.enums.OrderStatus;
import com.cnpm.repository.UserRepository;
import com.cnpm.repository.OrderRepository;
import com.cnpm.repository.PaymentRepository;
import com.cnpm.service.IOrderService;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService implements IOrderService {
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PaymentRepository paymentRepository;
	// Constructor injection
    public OrderService(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
    }
	public OrderDetailEmployeeDTO getOrderDetails(Long orderId) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new EntityNotFoundException("Order not found"));
		System.out.println(order.toString());
		// Chuyển đổi sang DTO để trả về thông tin chi tiết
		return mapToOrderDetailDTO(order);
	}
	private OrderLineDTO mapOrderLineToDTO(OrderLine orderLine) {
		 return OrderLineDTO.builder()
	                .productId(orderLine.getProduct().getProductId())
	                .productName(orderLine.getProduct().getProductName())
	                .quantity(orderLine.getQuantity())
	                .price(orderLine.getProduct().getCost())
	                .totalPrice(orderLine.getProduct().getCost() * orderLine.getQuantity())
	                .build();
	}

	private OrderDetailEmployeeDTO mapToOrderDetailDTO(Order order) {
		// Lấy thông tin khách hàng từ cơ sở dữ liệu (giả sử có một phương thức lấy
		// thông tin khách hàng)
		User customer = getUserById(order.getCustomerId());
		  // Lấy thông tin thanh toán
	    Payment payment = paymentRepository.findByOrder_OrderId(order.getOrderId());

		return OrderDetailEmployeeDTO.builder().orderId(order.getOrderId()).orderDate(order.getOrderDate())
				.shippingAddress(order.getShippingAddress()).orderStatus(order.getOrderStatus().toString())
				.deliveryDate(order.getDeliveryDate()).customerId(order.getCustomerId()) // Gán ID khách hàng
				.customerName(customer.getFullName()) // Gán tên khách hàng
				.accountNumber(String.valueOf(customer.getAccount().getAccountId()))
				.orderLines(order.getOrderLines().stream().map(this::mapOrderLineToDTO) // Chuyển đổi từng OrderLine																						// sang DTO
						.collect(Collectors.toSet()))
				// Thông tin thanh toán
	            .paymentMethod(payment != null ? payment.getPaymentMethod() : null)
	            .paymentStatus(payment != null ? payment.getPaymentStatus().toString() : null)
	            .paymentDate(payment != null ? payment.getPaymentDate().toString() : null)
	            .paymentTotal(payment != null ? payment.getTotal() : null)
	            .build();
	}

	private User getUserById(Long customerId) {
		System.out.println(customerId);
		   return userRepository.findById(customerId).orElse(null);
	}

	@Override
	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	@Override
	public Order getOrderById(Long id) {
		return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
	}

	@Override
	public void saveOrder(Order order) {
		orderRepository.save(order);
	}

	// Cập nhật thông tin trạng thái đơn hàng
	@Override
	public void updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        order.setOrderStatus(OrderStatus.valueOf(status)); // Cập nhật trạng thái
        orderRepository.save(order); // Lưu thay đổi
    }

	@Override
	public List<Order> getOrdersByStatus(OrderStatus status) {
		// Lấy danh sách đơn hàng theo trạng thái
		return orderRepository.findByOrderStatus(status);
	}

	// Lấy đơn hàng theo ID
	public Optional<Order> getOrderOptional(Long id) {
		return orderRepository.findById(id);
	}

	// Xóa đơn hàng theo ID
	public void deleteOrder(Long id) {
		orderRepository.deleteById(id);
	}
}
