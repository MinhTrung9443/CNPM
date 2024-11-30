package com.cnpm.service.interfaces;

import com.cnpm.entity.OrderLine;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public interface IOrderLineService{

    // Lấy tất cả các dòng đơn hàng
    Set<OrderLine> getAllOrderLines();

    // Cập nhật trạng thái đơn hàng (ví dụ theo mã dòng đơn hàng)
    void updateOrderLineStatus(Long orderLineId, String status);
}
