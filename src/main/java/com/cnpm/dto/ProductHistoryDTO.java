package com.cnpm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductHistoryDTO {
    private String productId;
    private String productName;
    private Integer quantity;
    private String image;
    private Double cost;
    private String category;
}
