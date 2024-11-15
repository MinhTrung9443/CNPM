package com.cnpm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class ProductFeedback implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productFeedbackId;
    @Column(length = 2000, columnDefinition = "nvarchar(2000)")
    private String comment;
    private LocalDateTime feedbackDate;
    private Long customerId;
    private Double rating;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "productId", referencedColumnName = "productId")
    private Product product;
}
