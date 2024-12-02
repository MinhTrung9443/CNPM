package com.cnpm.service;

import java.util.List;

import com.cnpm.entity.Payment;

public interface IRevenueService {
	List<Payment> getPayment(String month);

	double getTotal(String month);

	List<String> getAvailableMonths();
}
