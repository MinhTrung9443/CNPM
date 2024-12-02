package com.cnpm.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cnpm.entity.OrderLine;
import com.cnpm.enums.OrderStatus;
import com.cnpm.enums.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevenueDTO {
    private Long paymentId;
    private LocalDateTime paymentDate;
    private String paymentMethod;
    private PaymentStatus paymentStatus;
    private Double totalPayment;

    private Long orderId;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private String shippingAddress;

    private Set<OrderLine> orderLines=new HashSet<>();
}
