package com.cnpm.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartItemForOrderDTO {
    private String productCode;
    private Long quantity;
}
