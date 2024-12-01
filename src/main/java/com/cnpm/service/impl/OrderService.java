package com.cnpm.service.impl;

import com.cnpm.dto.ProductHistoryDTO;
import com.cnpm.dto.PurchaseHistoryDTO;
import com.cnpm.enums.OrderStatus;
import com.cnpm.enums.PaymentMethod;
import com.cnpm.repository.OrderRepository;
import com.cnpm.dto.CartItemForOrderDTO;
import com.cnpm.dto.CreateOrderRequest;
import com.cnpm.dto.OrderDetailEmployeeDTO;
import com.cnpm.dto.OrderLineDTO;
import com.cnpm.dto.OrderResponse;
import com.cnpm.entity.*;
import com.cnpm.enums.PaymentStatus;
import com.cnpm.repository.*;
import com.cnpm.service.interfaces.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cnpm.service.payment.VNPAYService;
import com.cnpm.util.Logger;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityNotFoundException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
	CartService cartService;
	@Autowired
	VNPAYService vnPayService;
	@Autowired
	private CartRepository cartRepository;

	@Transactional
	public OrderResponse createOrder(CreateOrderRequest createOrderRequest) {
		// TODO: chưa xử lý trường hợp tranh nhau đặt hàng
		// Tìm người dùng
		User user = userRepository.findById(createOrderRequest.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
		// Khởi tạo đơn hàng và các dòng đơn hàng
		Order order = new Order();
		Set<CartItemForOrderDTO> cartItemForOrderDTOS = createOrderRequest.getCartItemForOrderDTOS();
		Set<OrderLine> orderLines = new LinkedHashSet<>();
		// Lặp qua các mặt hàng trong giỏ hàng và tạo OrderLine
		double total = 0.0;
		for (var cartItem : cartItemForOrderDTOS) {
			Product product = productRepository.findFirstByProductCode(cartItem.getProductCode()).orElseThrow(() -> new RuntimeException("Product not found"));
			// check quantity
			if (productService.getStockByProductCode(product.getProductCode()) < cartItem.getQuantity()) {
				throw new RuntimeException("Product " + product.getProductCode() + " out of stock");
			}
//          đánh dấu sản phẩm đã được mua, số sp được đánh dấu là số lượng mua
			for (int i = 0; i < cartItem.getQuantity(); i++) {
//                tìm và đánh dấu sản phẩm đã được mua
				Product product1 = productRepository.findFirstByProductCodeAndIsUsedFalse(product.getProductCode()).orElseThrow(() -> new RuntimeException("Product not found"));
				product1.setIsUsed(1);
				productRepository.save(product1);
			}
			OrderLine orderLine = new OrderLine();
			orderLine.setProduct(product);
			orderLine.setQuantity(Math.toIntExact(cartItem.getQuantity()));
			orderLine.setOrder(order);
			orderLines.add(orderLine);
			// Cộng dồn tổng giá trị
			total += product.getCost() * cartItem.getQuantity();
		}
//        apply discount
		if (createOrderRequest.getVoucherCodes() != null) {
			for (String voucherCode : createOrderRequest.getVoucherCodes()) {
				Voucher voucher = voucherRepository
						.findFirstByVoucherCodeAndIsUsedFalseAndStartDateBeforeAndEndDateAfter(voucherCode,
								LocalDateTime.now(), LocalDateTime.now())
						.orElseThrow(() -> new RuntimeException("Voucher " + voucherCode + " is not available"));
				voucher.setIsUsed(true);
				voucherRepository.save(voucher);
				total -= voucher.getVoucherValue();
			}
		}
		// Thiết lập các thuộc tính cho đơn hàng
		order.setOrderLines(orderLines);
		order.setTotal(total < 0 ? 0 : total);
		order.setCustomerId(user.getUserId());
		order.setOrderDate(LocalDateTime.ofInstant(new Date().toInstant(), TimeZone.getDefault().toZoneId()));
		order.setOrderStatus(OrderStatus.PENDING);
		order.setShippingAddress(createOrderRequest.getAddress());

		// Lưu đơn hàng và tạo phản hồi
		Order savedOrder = orderRepository.save(order);

		// xoa cartitem
		Cart cart = cartService.getCartByUserId(createOrderRequest.getUserId());
		Set<CartItem> cartItems = cartService.getCartByUserId(createOrderRequest.getUserId()).getCartItems();
		Logger.log("ds cart"+cartItems.toString());
		for (CartItemForOrderDTO cartItemForOrderDTO : cartItemForOrderDTOS) {
//            cartService.removeItemFromCart(cart.getCartItems().stream().filter(cartItem1 -> cartItem1.getProduct().getProductCode().equals(cartItem.getProductCode())).findFirst().get());
			cartItems.removeIf(
					cartItem1 -> cartItem1.getProduct().getProductCode().equals(cartItemForOrderDTO.getProductCode()));
		}
		cart.setCartItems(cartItems);
		cartRepository.save(cart);

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


	@Transactional
	public OrderResponse createOrderForSingleProduct(CreateOrderRequest createOrderRequest) {
		// TODO: chưa xử lý trường hợp tranh nhau đặt hàng
		// Tìm người dùng
		User user = userRepository.findById(createOrderRequest.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
		// Khởi tạo đơn hàng và các dòng đơn hàng
		Order order = new Order();
		Set<CartItemForOrderDTO> cartItemForOrderDTOS = createOrderRequest.getCartItemForOrderDTOS();
		Set<OrderLine> orderLines = new LinkedHashSet<>();
		double total = 0.0;
		for (var cartItem : cartItemForOrderDTOS) {
			Product product = productRepository.findFirstByProductCode(cartItem.getProductCode())
					.orElseThrow(() -> new RuntimeException("Product not found"));
			// check quantity
			if (productService.getStockByProductCode(product.getProductCode()) < cartItem.getQuantity()) {
				throw new RuntimeException("Product " + product.getProductCode() + " out of stock");
			}
//          đánh dấu sản phẩm đã được mua, số sp được đánh dấu là số lượng mua
			for (int i = 0; i < cartItem.getQuantity(); i++) {
//                tìm và đánh dấu sản phẩm đã được mua
				Product product1 = productRepository.findFirstByProductCodeAndIsUsedFalse(product.getProductCode()).orElseThrow(() -> new RuntimeException("Product " + product.getProductCode() + " not found"));
				product1.setIsUsed(1);
				productRepository.save(product1);
			}
			OrderLine orderLine = new OrderLine();
			orderLine.setProduct(product);
			orderLine.setQuantity(Math.toIntExact(cartItem.getQuantity()));
			orderLine.setOrder(order);
			orderLines.add(orderLine);
			// Cộng dồn tổng giá trị
			total += product.getCost() * cartItem.getQuantity();
		}
//        apply discount
		if (createOrderRequest.getVoucherCodes() != null) {
			for (String voucherCode : createOrderRequest.getVoucherCodes()) {
				Voucher voucher = voucherRepository
						.findFirstByVoucherCodeAndIsUsedFalseAndStartDateBeforeAndEndDateAfter(voucherCode,
								LocalDateTime.now(), LocalDateTime.now())
						.orElseThrow(() -> new RuntimeException("Voucher " + voucherCode + " is not available"));
				voucher.setIsUsed(true);
				voucherRepository.save(voucher);
				total -= voucher.getVoucherValue();
			}
		}
		// Thiết lập các thuộc tính cho đơn hàng
		order.setOrderLines(orderLines);
		order.setTotal(total < 0 ? 0 : total);
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
	@Override
	public void updateOrderStatusPaymentTime(Long orderId, String paymentTime) {
		Logger.log("updating order status");
		Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
		order.setOrderStatus(OrderStatus.CONFIRMED);
		Payment payment = paymentRepository.findByOrder(order)
				.orElseThrow(() -> new RuntimeException("Payment not found"));
		payment.setPaymentStatus(PaymentStatus.PAID);
		payment.setPaymentDate(LocalDateTime.parse(paymentTime, DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
		paymentRepository.save(payment);
		orderRepository.save(order);
		Logger.log("updated order status");

	}

	@Override
	public void updateOrderStatus(Long orderId) {
		Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
		order.setOrderStatus(OrderStatus.CONFIRMED);
		Payment payment = paymentRepository.findByOrder(order)
				.orElseThrow(() -> new RuntimeException("Payment not found"));
		payment.setPaymentStatus(PaymentStatus.PENDING);
		payment.setPaymentDate(order.getOrderDate());
		paymentRepository.save(payment);
		orderRepository.save(order);
	}

	@Override
	public Double getOrderTotal(Long orderId) {
		Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
		return order.getTotal();
	}

	public OrderDetailEmployeeDTO getOrderDetails(Long orderId) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new EntityNotFoundException("Order not found"));
		System.out.println(order.toString());
		// Chuyển đổi sang DTO để trả về thông tin chi tiết
		return mapToOrderDetailDTO(order);
	}

	private OrderLineDTO mapOrderLineToDTO(OrderLine orderLine) {
		return OrderLineDTO.builder().productId(orderLine.getProduct().getProductId())
				.productName(orderLine.getProduct().getProductName()).quantity(orderLine.getQuantity())
				.price(orderLine.getProduct().getCost())
				.totalPrice(orderLine.getProduct().getCost() * orderLine.getQuantity()).build();
	}

	private OrderDetailEmployeeDTO mapToOrderDetailDTO(Order order) {
		// Lấy thông tin khách hàng từ cơ sở dữ liệu
		User customer = getUserById(order.getCustomerId());

		// Khai báo biến accountRefundId
		AccountRefund accountRefund = null;

		if (customer instanceof Customer) {
			Customer a = (Customer) customer;
			if (a.getAccountRefund() != null) {
				accountRefund = a.getAccountRefund(); // Lấy đối tượng AccountRefund
			}
		}

		// Lấy thông tin thanh toán
		Payment payment = paymentRepository.findByOrder_OrderId(order.getOrderId());

		// Trả về DTO với các giá trị cần thiết
		return OrderDetailEmployeeDTO.builder().orderId(order.getOrderId()).orderDate(order.getOrderDate())
				.shippingAddress(order.getShippingAddress()).orderStatus(order.getOrderStatus().toString())
				.deliveryDate(order.getDeliveryDate()).customerId(order.getCustomerId()) // Gán ID khách hàng
				.customerName(customer.getFullName()) // Gán tên khách hàng
				.phone(String.valueOf(customer.getPhone()))
				.orderLines(order.getOrderLines().stream().map(this::mapOrderLineToDTO) // Chuyển đổi từng OrderLine
																						// sang DTO
						.collect(Collectors.toSet()))
				.accountType(accountRefund != null ? String.valueOf(accountRefund.getType()) : null)
				.accountNum(accountRefund != null ? String.valueOf(accountRefund.getAccountNum()) : null)
				// accountRefundId
				// Thông tin thanh toán
				.paymentMethod(payment != null ? payment.getPaymentMethod().name() : null)
				.paymentStatus(payment != null ? payment.getPaymentStatus().toString() : null)
				.paymentDate(payment != null ? payment.getPaymentDate().toString() : null)
				.paymentTotal(payment != null ? payment.getTotal() : null).build();
	}

	private User getUserById(Long customerId) {
		System.out.println(customerId);
		return userRepository.findById(customerId).orElse(null);
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
	public boolean processRefund(Long orderId) {
		// Lấy đơn hàng từ cơ sở dữ liệu
		Order order = orderRepository.findById(orderId).orElse(null);

		if (order.getOrderStatus() == OrderStatus.CANCELLED) {
			// Cập nhật trạng thái đơn hàng thành 'REFUND'
			order.setOrderStatus(OrderStatus.REFUNDED);
			orderRepository.save(order);
			return true;
		}

		return false; // Nếu không tìm thấy đơn hàng hoặc đơn hàng đã bị hủy
	}

	// Lấy đơn hàng theo ID
	public Optional<Order> getOrderOptional(Long id) {
		return orderRepository.findById(id);
	}

	// Xóa đơn hàng theo ID
	public void deleteOrder(Long id) {
		orderRepository.deleteById(id);
	}

	@Override
	public Optional<Order> findById(Long id) {
		return orderRepository.findById(id);
	}

	@Override
	public <S extends Order> S save(S entity) {
		return orderRepository.save(entity);
	}

	@Autowired
	public OrderService(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public PurchaseHistoryDTO mapperToPurchaseHistoryDTO(Order order) {
		Set<ProductHistoryDTO> productHistoryDTOs = order.getOrderLines().stream().map(orderLine -> {
			Product product = orderLine.getProduct();
			if (product != null) {
				return new ProductHistoryDTO(product.getProductId(),product.getProductCode(), product.getProductName(),
						orderLine.getQuantity(), product.getImage(), product.getCost(), product.getCategory());
			}
			return null;
		}).filter(productHistoryDTO -> productHistoryDTO != null).collect(Collectors.toSet());
		// Tính tổng chi phí đơn hàng
		double totalCost = productHistoryDTOs.stream().mapToDouble(product -> product.getCost() * product.getQuantity())
				.sum();
		return new PurchaseHistoryDTO(order.getOrderId(), order.getOrderDate(), order.getShippingAddress(),
				order.getOrderStatus(), productHistoryDTOs, totalCost);
	}

	@Override
	public Page<PurchaseHistoryDTO> getAllOrders(Long customerId, Pageable pageable) {
		// Get a paginated result from the repository
		Page<Order> ordersPage = orderRepository.findByCustomerId(customerId, pageable);

		// Convert the paginated Order objects into PurchaseHistoryDTOs
		return ordersPage.map(this::mapperToPurchaseHistoryDTO); // Using a mapper to convert each Order into
																	// PurchaseHistoryDTO
	}

	@Override
	public Set<PurchaseHistoryDTO> getPurchaseHistory(Long customerId, OrderStatus status) {
		Set<PurchaseHistoryDTO> historyDTOs = new HashSet<>();

		// Lấy các đơn hàng từ DB dựa vào customerId và trạng thái
		Set<Order> orders = orderRepository.findByCustomerIdAndOrderStatus(customerId, status);

		for (Order order : orders) {
			historyDTOs.add(mapperToPurchaseHistoryDTO(order));
		}

		return historyDTOs;
	}

	public Page<Order> getOrdersByStatus(OrderStatus status, Pageable pageable) {
		return orderRepository.findByOrderStatus(status, pageable);
	}

	public Page<Order> getCancelledAndRefundedOrders(Pageable pageable) {
		return orderRepository.findByOrderStatusIn(Arrays.asList(OrderStatus.CANCELLED, OrderStatus.REFUNDED),
				pageable);
	}

	@Override
	public Page<Order> getAllOrders(Pageable pageable) {
		return orderRepository.findAll(pageable);
	}

	@Override
	public Set<PurchaseHistoryDTO> getAllOrders(Long customerId) {
		Set<Order> orders = orderRepository.findByCustomerId(customerId); // Lấy tất cả đơn hàng
		return orders.stream().map(this::mapperToPurchaseHistoryDTO).collect(Collectors.toSet()); // Chuyển đổi
	}

	public PaymentMethod getPaymentMethodByOrderId(Long orderId) {
		Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
		Payment payment = paymentRepository.findByOrder(order)
				.orElseThrow(() -> new RuntimeException("Payment not found"));
		return payment.getPaymentMethod();
	}
}
