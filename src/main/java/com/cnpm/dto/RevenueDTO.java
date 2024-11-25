package com.cnpm.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.cnpm.entity.OrderLine;
import com.cnpm.enums.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevenueDTO {
    private Long paymentId;        // (Who's paid)
    private LocalDateTime paymentDate; 
    private String paymentMethod;  //   (How)
    private PaymentStatus paymentStatus;  
    private Double totalPayment;      //  (How much)
    
    private Long orderId;           //  (What's paid)
    private LocalDateTime orderDate; // 
    private String orderStatus;     // 
    private String shippingAddress; //

    private List<OrderLine> orderLines;
}