package com.cnpm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class ShopOwner extends User implements Serializable {
    @Column(length = 100, columnDefinition = "nvarchar(100)")
    private String shopName;
}
