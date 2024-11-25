package com.cnpm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnpm.dto.RevenueDTO;
import com.cnpm.entity.Order;
import com.cnpm.entity.OrderLine;
import com.cnpm.entity.Payment;
import com.cnpm.repository.PaymentRepository;
import com.cnpm.service.IRevenueService;

@Service
public class RevenueService implements IRevenueService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public List<RevenueDTO> getRevenue(String month, String category) {
        List<Payment> payments;

        if (month != null && !month.isEmpty()) {
            // Lọc theo tháng
            payments = paymentRepository.findPaymentsByMonth(month);
        } else if (category != null && !category.isEmpty()) {
            // Lọc theo danh mục
            payments = paymentRepository.findPaymentsByCategory(category);
        } else {
            // Không có tiêu chí -> lấy tất cả
            payments = paymentRepository.findAllPayments();
        }

        return payments.stream().map(payment -> {
            Order order = payment.getOrder();
            List<OrderLine> orderLines = order.getOrderLines().stream().toList();

            return new RevenueDTO(
                payment.getPaymentId(),
                payment.getPaymentDate(),
                payment.getPaymentMethod(),
                payment.getPaymentStatus(),
                payment.getTotal(),
                order.getOrderId(),
                order.getOrderDate(),
                order.getOrderStatus().toString(),
                order.getShippingAddress(),
                orderLines
            );
        }).toList();
    }
    
    public double getTotalRevenue(String month, String category) {
        List<Payment> payments;

        if (month != null && !month.isEmpty()) {
            payments = paymentRepository.findPaymentsByMonth(month);
        } else if (category != null && !category.isEmpty()) {
            payments = paymentRepository.findPaymentsByCategory(category);
        } else {
            payments = paymentRepository.findAllPayments();
        }

        return payments.stream()
                       .mapToDouble(Payment::getTotal)  // Lấy tổng tiền của từng Payment
                       .sum();  
    }
}
