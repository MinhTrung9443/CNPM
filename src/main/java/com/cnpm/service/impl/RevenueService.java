package com.cnpm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnpm.entity.Payment;
import com.cnpm.repository.PaymentRepository;
import com.cnpm.service.IRevenueService;

@Service
public class RevenueService implements IRevenueService {

	@Autowired
	private PaymentRepository paymentRepository;

	@Override
	public List<Payment> getPayment(String month) {
		return paymentRepository.findPaymentsByMonth(month);
	}

	@Override
	public double getTotal(String month) {
		List<Payment> payments = paymentRepository.findPaymentsByMonth(month);

		return payments.stream().mapToDouble(payment -> {
			return payment.getTotal();
		}).sum();
	}

	@Override
	public List<String> getAvailableMonths() {
		return paymentRepository.findDistinctMonths();
	}
}
