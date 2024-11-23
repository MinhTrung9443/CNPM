package com.cnpm.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import com.cnpm.entity.ProductFeedback;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDTO {
    private String productCode;
    private String productName;
    private String category;
    private Double cost;
    private String description;
    private String brand;
    private LocalDate manufactureDate;
    private LocalDate expirationDate;
    private String ingredient;
    private String how_to_use;
    private String volume;
    private String origin;
    private String image;
    private Long stock;
    private List<ProductFeedback> productFeedbacks; 
}
