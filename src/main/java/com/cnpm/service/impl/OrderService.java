package com.cnpm.service.impl;

import com.cnpm.dto.ProductHistoryDTO;
import com.cnpm.dto.PurchaseHistoryDTO;
import com.cnpm.entity.Order;
import com.cnpm.entity.OrderLine;
import com.cnpm.entity.Product;
import com.cnpm.enums.OrderStatus;
import com.cnpm.repository.OrderRepository;
import com.cnpm.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public PurchaseHistoryDTO mapperToPurchaseHistoryDTO(Order order) {
        Set<ProductHistoryDTO> productHistoryDTOs = order.getOrderLines().stream()
                .map(orderLine -> {
                    Product product = orderLine.getProduct();
                    if (product != null) {
                        return new ProductHistoryDTO(
                                product.getProductId(),
                                product.getProductName(),
                                orderLine.getQuantity(),
                                product.getImage(),
                                product.getCost(),
                                product.getCategory()
                        );
                    }
                    return null;
                })
                .filter(productHistoryDTO -> productHistoryDTO != null)
                .collect(Collectors.toSet());
        // Tính tổng chi phí đơn hàng
        double totalCost = productHistoryDTOs.stream()
                .mapToDouble(product -> product.getCost() * product.getQuantity())
                .sum();
        return new PurchaseHistoryDTO(
                order.getOrderId(),
                order.getOrderDate(),
                order.getShippingAddress(),
                order.getOrderStatus(),
                productHistoryDTOs,
                totalCost
        );
    }


    @Override
    public Set<PurchaseHistoryDTO> getAllOrders(Long customerId) {
        Set<PurchaseHistoryDTO> allOrdersDTOs = new HashSet<>();

        Set<Order> orders = orderRepository.findByCustomerId(customerId);

        for (Order order : orders) {
            allOrdersDTOs.add(mapperToPurchaseHistoryDTO(order));
        }

        return allOrdersDTOs;
    }

    @Override
    public Set<PurchaseHistoryDTO> getPurchaseHistory(Long customerId, OrderStatus status) {
        Set<PurchaseHistoryDTO> historyDTOs = new HashSet<>();

        // Lấy các đơn hàng từ DB dựa vào customerId và trạng thái
        Set<Order> orders = orderRepository.findByCustomerIdAndOrderStatus(customerId, status);

        for (Order order : orders) {
            historyDTOs.add(mapperToPurchaseHistoryDTO(order));
        }

        return historyDTOs;
    }
}
