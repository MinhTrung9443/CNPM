package com.cnpm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Employee extends User implements Serializable {
    @Column(unique = true)
    private Long employeeId; // This is atrribute is not a primary key in the database
    private LocalDate startDate;
}
