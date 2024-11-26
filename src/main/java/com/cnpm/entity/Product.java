package com.cnpm.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate manufactureDate;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate expirationDate;
    @Column(columnDefinition = "nvarchar(max)")
    private String ingredient;
    @Column(columnDefinition = "nvarchar(max)")
    private String how_to_use;
    private String volume;
    @Column(columnDefinition = "nvarchar(max)")
    private String origin;
    @Column(columnDefinition = "nvarchar(max)")
    private String image;
    @Column(nullable = false, columnDefinition = "int default 0")
    private int isUsed;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "product")
    @JsonBackReference
    private List<CartItem> cartItems; // Inverse side, optional, when we want to navigate from CartItem to Product

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "product")
    @JsonBackReference
    private List<OrderLine> orderLines; // Inverse side, optional, when we want to navigate from OrderLine to Product


}
