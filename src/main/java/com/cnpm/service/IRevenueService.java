package com.cnpm.service;

import java.util.List;

import com.cnpm.dto.RevenueDTO;

public interface IRevenueService {
	List<RevenueDTO> getRevenue(String month);

	double getTotalRevenue(String month);

	List<String> getAvailableMonths();
}
