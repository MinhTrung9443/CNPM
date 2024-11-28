package com.cnpm.dto;

import java.util.Set;


import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDetailEmployeeDTO {
	  private Long orderId;
	    private LocalDateTime orderDate;
	    private String shippingAddress;
	    private String orderStatus;
	    private LocalDateTime deliveryDate;
	    private Long customerId; 
	    private String customerName;
	    private String phone;
	    // Thông tin sản phẩm
	    private Set<OrderLineDTO> orderLines;
	 // Thông tin thanh toán
	    private String accountRefundId;
	    private String paymentMethod;
	    private String paymentStatus;
	    private String paymentDate;
	    private Double paymentTotal;
}
