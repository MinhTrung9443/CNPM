package com.cnpm.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @Column(columnDefinition = "nvarchar(max)")
    private String productName;
    @Column(columnDefinition = "nvarchar(max)")
    private String category;
    private Double cost;
    @Column(columnDefinition = "nvarchar(max)")
    private String description;
    @Column(columnDefinition = "nvarchar(max)")
    private String brand;
    private LocalDate manufactureDate;
    private LocalDate expirationDate;
    @Column(columnDefinition = "nvarchar(max)")
    private String ingredient;
    @Column(columnDefinition = "nvarchar(max)")
    private String how_to_use;
    private String volume;
    @Column(columnDefinition = "nvarchar(50)")
    private String origin;
    @Column(columnDefinition = "nvarchar(max)")
    private String image;



    @OneToMany(mappedBy = "product")
    @JsonBackReference
    private List<CartItem> cartItems; // Inverse side, optional, when we want to navigate from CartItem to Product

    @OneToMany(mappedBy = "product")
    @JsonBackReference
    private List<OrderLine> orderLines; // Inverse side, optional, when we want to navigate from OrderLine to Product


}
