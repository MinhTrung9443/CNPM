package com.cnpm.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Customer extends User implements Serializable {
	@DateTimeFormat (pattern="yyyy-MM-dd")
    private LocalDate birthDate;
}
