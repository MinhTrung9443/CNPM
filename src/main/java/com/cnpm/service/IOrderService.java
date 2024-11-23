package com.cnpm.service;

import com.cnpm.dto.PurchaseHistoryDTO;
import com.cnpm.entity.Order;
import com.cnpm.enums.OrderStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface IOrderService{
    public Set<PurchaseHistoryDTO> getAllOrders(Long customerId);

    public Set<PurchaseHistoryDTO> getPurchaseHistory(Long customerId, OrderStatus status);

}
