package com.cnpm.service.interfaces;

import org.springframework.stereotype.Service;

import com.cnpm.entity.Payment;

@Service 
public interface IPaymetService {

	Payment findByOrder_OrderId(Long orderId);

}
