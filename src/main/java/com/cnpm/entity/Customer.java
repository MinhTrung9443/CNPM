package com.cnpm.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@PrimaryKeyJoinColumn(name = "userId")
public class Customer extends User implements Serializable {
	@DateTimeFormat (pattern="yyyy-MM-dd")
    private LocalDate birthDate;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "accountRefundId")
	@JsonManagedReference
	private AccountRefund accountRefund;
}
