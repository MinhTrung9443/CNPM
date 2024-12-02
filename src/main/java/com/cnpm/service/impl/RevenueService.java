package com.cnpm.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnpm.dto.RevenueDTO;
import com.cnpm.entity.Order;
import com.cnpm.entity.OrderLine;
import com.cnpm.entity.Payment;
import com.cnpm.repository.PaymentRepository;
import com.cnpm.service.IRevenueService;

@Service
public class RevenueService implements IRevenueService {

	@Autowired
	private PaymentRepository paymentRepository;

	@Override
	public List<RevenueDTO> getRevenue(String month) {
		List<Payment> payments = paymentRepository.findPaymentsByMonth(month);

		return payments.stream().map(payment -> {
			Order o = payment.getOrder();
			String shippingAddress = o.getShippingAddress();

			Set<OrderLine> orderLines = o.getOrderLines();

			return new RevenueDTO(payment.getPaymentId(), payment.getPaymentDate(), payment.getPaymentMethod(),
					payment.getPaymentStatus(), payment.getTotal(), o.getOrderId(), o.getOrderDate(),
					o.getOrderStatus(), shippingAddress, orderLines);
		}).collect(Collectors.toList());
	}

	@Override
	public double getTotalRevenue(String month) {
		List<Payment> payments = paymentRepository.findPaymentsByMonth(month);

	    return payments.stream()
	                   .mapToDouble(payment -> {
	                       return payment.getTotal();
	                   })
	                   .sum();
	}

	@Override
	public List<String> getAvailableMonths() {
		return paymentRepository.findDistinctMonths();
	}
}
