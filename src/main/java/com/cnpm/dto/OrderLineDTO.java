package com.cnpm.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderLineDTO {
	  private Long productId;
	    private String productName;
	    private Integer quantity;
	    private Double price;
	    private Double totalPrice;
	  
}
