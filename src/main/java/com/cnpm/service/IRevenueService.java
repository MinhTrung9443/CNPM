package com.cnpm.service;

import java.util.List;

import com.cnpm.dto.RevenueDTO;

public interface IRevenueService {
	List<RevenueDTO> getRevenue(String month, String category);

	double getTotalRevenue(String month, String category);

	List<String> getAvailableMonths();

	List<String> getAvailableCategory();
}
