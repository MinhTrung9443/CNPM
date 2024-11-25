package com.cnpm.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import com.cnpm.entity.ProductFeedback;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDTO {
    private String productCode;
    @NotEmpty(message="The name is required")
    private String productName;
    @NotEmpty(message="The category is required")
    private String category;
    @Min(0)
    private Double cost;
    @Size(min=10, message="The description should be at least 10 characters")
    @Size(max=2000, message = "The description cannot exceed 2000 characters")
    private String description;
    @NotEmpty(message="The brand is required")
    private String brand;
    @NotNull(message = "The manufacture date is required")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate manufactureDate;
    @NotNull(message = "The expiration date is required")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate expirationDate;
    @NotEmpty(message="The ingredient is required")
    private String ingredient;
    @Size(min=10, message="The instruction should be at least 10 characters")
    @Size(max=2000, message = "The instruction cannot exceed 2000 characters")
    private String how_to_use;
    @NotEmpty(message="The volume is required")
    private String volume;
    @NotEmpty(message="The origin is required")
    private String origin;
    private MultipartFile image;
    @Min(1)
    private Long stock;
    private List<ProductFeedback> productFeedbacks; 
}
