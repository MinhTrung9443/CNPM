package com.cnpm.dto;

import com.cnpm.enums.PaymentMethod;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class OrderResponse {
    private Long orderId;
    private int total;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
//    private String url;
}
