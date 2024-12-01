package com.cnpm.controller.employee;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.cnpm.dto.OrderDetailEmployeeDTO;
import com.cnpm.entity.Order;
import com.cnpm.enums.OrderStatus;
import com.cnpm.service.interfaces.IOrderService;

@Controller
@RequestMapping("/employee/order-management")
public class ManageOrderController {

	private final IOrderService orderService;

	@Autowired
	public ManageOrderController(IOrderService orderService) {
		this.orderService = orderService;
	}

	// Hiển thị trang quản lý đơn hàng
	@GetMapping("")
	public String getOrderPage(
			@RequestParam(value = "tab", required = false, defaultValue = "tat-ca-don-hang") String tab,
			@RequestParam(value = "page", defaultValue = "1") Optional<Integer> page,
			@RequestParam(value = "size", defaultValue = "5") Optional<Integer> size, Model model) {

		int currentPage = page.orElse(1);
		int pageSize = size.orElse(5);

		Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
		Page<Order> orderPage;

		// Lọc đơn hàng theo trạng thái và áp dụng phân trang
		switch (tab) {
		case "tat-ca-don-hang":
			orderPage = orderService.getAllOrders(pageable); // Tất cả đơn hàng
			break;
		case "don-cho-xac-nhan":
			orderPage = orderService.getOrdersByStatus(OrderStatus.PENDING, pageable); // Đơn chờ xác nhận
			break;
		case "don-da-xac-nhan":
			orderPage = orderService.getOrdersByStatus(OrderStatus.CONFIRMED, pageable); // Đơn đã xác nhận
			break;
		case "don-dang-van-chuyen":
			orderPage = orderService.getOrdersByStatus(OrderStatus.SHIPPING, pageable); // Đơn đang vận chuyển
			break;
		case "don-da-giao":
			orderPage = orderService.getOrdersByStatus(OrderStatus.COMPLETED, pageable); // Đơn đã giao
			break;
		case "don-huy":
			orderPage = orderService.getCancelledAndRefundedOrders(pageable); // Đơn hủy và hoàn tiền
			break;
		default:
			orderPage = orderService.getAllOrders(pageable); // Mặc định là tất cả đơn hàng
			break;
		}

		// Tính toán các số trang để hiển thị
		int totalPages = orderPage.getTotalPages();
		int start = Math.max(1, currentPage - 2);
		int end = Math.min(currentPage + 2, totalPages);
		List<Integer> pageNumbers = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());

		model.addAttribute("Orders", orderPage.getContent());
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("pageNumbers", pageNumbers);
		model.addAttribute("tab", tab);

		return "employee/order-management";
	}

	@PostMapping
	public String updateOrderStatus(@RequestParam Long orderId, @RequestParam String status) {
		orderService.updateOrderStatus(orderId, status);
		return "redirect:/employee/order-management";
	}

	@PostMapping("/refund")
	public String processRefund(@RequestParam("orderId") Long orderId, @RequestParam("status") String status,
			Model model) {
		if ("REFUND".equalsIgnoreCase(status)) {
			boolean refundSuccess = orderService.processRefund(orderId);
			model.addAttribute("message", refundSuccess ? "Hoàn tiền thành công!" : "Lỗi trong quá trình hoàn tiền.");
		} else {
			model.addAttribute("message", "Trạng thái không hợp lệ.");
		}
		return "redirect:/employee/order-management";
	}

	@GetMapping("/details/{orderId}")
	public String viewOrderDetails(@PathVariable Long orderId, Model model) {
		if (orderId != null) {
			OrderDetailEmployeeDTO orderDetails = orderService.getOrderDetails(orderId);
			model.addAttribute("orderDetails", orderDetails);
			return "employee/order-details";
		} else {
			return "redirect:/employee/order-management";
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
		Order existingOrder = orderService.getOrderById(id);
		if (existingOrder != null) {
			orderService.deleteOrder(id);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping("/search")
	public String searchOrdersByDate(
			@RequestParam("orderDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate orderDate,
			Model model) {

		// Chuyển đổi LocalDate thành LocalDateTime (bắt đầu của ngày)
		LocalDateTime startOfDay = orderDate.atStartOfDay();
		LocalDateTime endOfDay = orderDate.atTime(23, 59, 59, 999999999); // Cuối ngày

		// Tìm các đơn hàng theo ngày (trong repository)
		List<Order> orders = orderService.findOrdersByDateRange(startOfDay, endOfDay);

		// Thêm danh sách đơn hàng vào model để hiển thị trong view
		model.addAttribute("Orders", orders); // Đảm bảo tên biến "Orders" thống nhất
		model.addAttribute("searchDate", orderDate.toString());

		// Trả về view để hiển thị kết quả tìm kiếm
		return "employee/order-management-search"; // Đảm bảo đường dẫn view đúng
	}

}
