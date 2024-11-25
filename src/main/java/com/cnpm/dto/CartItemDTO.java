package com.cnpm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartItemDTO {
    private String productCode;
    private String productName;
    private double cost;
    private int quantity;
    private String image;
}
