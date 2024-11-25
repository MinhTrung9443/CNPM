package com.cnpm.dto;

import com.cnpm.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PurchaseHistoryDTO {
    private Long orderId;
    private LocalDateTime orderDate;
    private String shippingAddress;
    private OrderStatus orderStatus;
    private Set<ProductHistoryDTO> products;
    private Double totalCost;
}
