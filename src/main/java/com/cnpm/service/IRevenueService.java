package com.cnpm.service;

import java.util.List;

import com.cnpm.entity.Payment;

public interface IRevenueService {
	List<Payment> getRevenue(String month);

	double getTotalRevenue(String month);

	List<String> getAvailableMonths();
}
