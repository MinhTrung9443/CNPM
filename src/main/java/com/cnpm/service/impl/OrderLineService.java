package com.cnpm.service.impl;

import com.cnpm.entity.OrderLine;
import com.cnpm.repository.OrderLineRepository;
import com.cnpm.service.IOrderLineService;
import com.cnpm.entity.Order;
import com.cnpm.enums.OrderStatus;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderLineService implements IOrderLineService {

    @Autowired
    private OrderLineRepository orderLineRepository;

    // Lấy tất cả các dòng đơn hàng
    public Set<OrderLine> getAllOrderLines() {
        return (Set<OrderLine>) orderLineRepository.findAll();
    }

    // Cập nhật trạng thái đơn hàng (ví dụ theo mã dòng đơn hàng)
    public void updateOrderLineStatus(Long orderLineId, String status) {
        // Kiểm tra đơn hàng có tồn tại không
        OrderLine orderLine = orderLineRepository.findById(orderLineId)
            .orElseThrow(() -> new RuntimeException("Order line not found"));

        // Lấy đối tượng Order từ OrderLine
        Order order = orderLine.getOrder();
        
        // Cập nhật trạng thái đơn hàng (OrderStatus là Enum, bạn có thể chuyển String thành Enum)
        try {
            OrderStatus newStatus = OrderStatus.valueOf(status);
            order.setOrderStatus(newStatus);  // Cập nhật trạng thái cho Order
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status value: " + status);
        }

        // Lưu lại thay đổi trạng thái trong Order
        // Lưu cả OrderLine và Order để cập nhật trạng thái vào cơ sở dữ liệu
        orderLineRepository.save(orderLine);  // Nếu bạn muốn lưu OrderLine (nếu có thay đổi khác)
    }
}
