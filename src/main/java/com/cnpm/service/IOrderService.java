package com.cnpm.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public interface IOrderService{

    void updateOrderStatus(Long orderId, String paymentTime);

    void updateOrderStatus(Long orderId);

    Double getOrderTotal(Long orderId);
}
