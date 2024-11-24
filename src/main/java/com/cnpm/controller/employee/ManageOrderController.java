package com.cnpm.controller.employee;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.cnpm.dto.OrderDetailEmployeeDTO;
import com.cnpm.entity.Order;
import com.cnpm.enums.OrderStatus;
import com.cnpm.service.IOrderService;


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
			@RequestParam(value = "tab", required = false, defaultValue = "tat-ca-don-hang") String tab, Model model) {
		List<Order> orders;

		// Lọc đơn hàng theo trạng thái
		switch (tab) {
		case "tat-ca-don-hang":
			orders = orderService.getAllOrders(); // Tất cả đơn hàng
			break;
		case "don-cho-xac-nhan":
			orders = orderService.getOrdersByStatus(OrderStatus.PENDING); // Đơn chờ xác nhận
			break;
		case "don-da-xac-nhan":
			orders = orderService.getOrdersByStatus(OrderStatus.CONFIRMED); // Đơn chờ xác nhận
			break;
		case "don-dang-van-chuyen":
			orders = orderService.getOrdersByStatus(OrderStatus.SHIPPING); // Đơn đang vận chuyển
			break;
		case "don-da-giao":
			orders = orderService.getOrdersByStatus(OrderStatus.COMPLETED); // Đơn đã giao
			break;
		case "don-huy":
			orders = orderService.getOrdersByStatus(OrderStatus.CANCELLED); // Đơn hủy
			break;
		default:
			orders = orderService.getAllOrders(); // Mặc định là tất cả đơn hàng
			break;
		}

		model.addAttribute("Orders", orders);
		model.addAttribute("tab", tab); // Thêm biến tab vào model để giữ trạng thái tab
		return "employee/order-management"; // Trả về trang HTML
	}

	@PostMapping
	public String updateOrderStatus(@RequestParam Long orderId, @RequestParam String status) {
		orderService.updateOrderStatus(orderId, status); // Gọi service cập nhật
		return "redirect:/employee/order-management"; // Reload trang sau khi cập nhật
	}
	  // Phương thức hoàn tiền
    @PostMapping("/refund")
    public String processRefund(@RequestParam("orderId") Long orderId, @RequestParam("status") String status, Model model) {
    	System.out.println(orderId);
        // Kiểm tra xem trạng thái có phải là 'REFUND' hay không
        if ("REFUND".equalsIgnoreCase(status)) {
            // Cập nhật trạng thái đơn hàng là hoàn tiền
            boolean refundSuccess = orderService.processRefund(orderId);

            if (refundSuccess) {
                model.addAttribute("message", "Hoàn tiền thành công!");
            } else {
                model.addAttribute("message", "Lỗi trong quá trình hoàn tiền.");
            }
        } else {
            model.addAttribute("message", "Trạng thái không hợp lệ.");
        }

        // Trả về trang quản lý đơn hàng sau khi xử lý
        return "redirect:/employee/order-management";
    }

	@GetMapping("/details/{orderId}")
	public String viewOrderDetails(@PathVariable Long orderId, Model model) {
		if (orderId != null) {
		

			OrderDetailEmployeeDTO orderDetails = orderService.getOrderDetails(orderId);

			model.addAttribute("orderDetails", orderDetails);

			return "employee/order-details"; // Tên file HTML Thymeleaf để hiển thị
		} else {
			System.out.println("alllllo");
			return "";
		}

	}

	// Xóa đơn hàng
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
		Order existingOrder = orderService.getOrderById(id);
		if (existingOrder != null) {
			orderService.deleteOrder(id); // Gọi service để xóa đơn hàng
			return ResponseEntity.noContent().build(); // Trả về HTTP 204 - No Content
		}
		return ResponseEntity.notFound().build(); // Trả về HTTP 404 nếu không tìm thấy đơn hàng
	}
}
