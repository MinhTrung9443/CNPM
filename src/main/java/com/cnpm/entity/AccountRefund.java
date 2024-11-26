package com.cnpm.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class AccountRefund {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long accountRefundId;
	private String type;
	private String accountNum;
	
	@OneToOne(mappedBy = "accountRefund")
	@JsonBackReference
	private Customer customer;
}	
