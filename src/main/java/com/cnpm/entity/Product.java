package com.cnpm.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String productCode;
    private String productName;
    private Double cost;
    private String description;
    private String brand;
    private LocalDate manufactureDate;
    private LocalDate expiryDate;
    private String ingredient;
    private String how_to_use;
    private String volume;
    private String origin;
    private String image;

    @OneToMany
    @JsonBackReference
    private List<CartItem> cartItems; // Inverse side, optional, when we want to navigate from CartItem to Product

    @OneToMany
    @JsonBackReference
    private List<OrderLine> orderLines; // Inverse side, optional, when we want to navigate from OrderLine to Product

}
