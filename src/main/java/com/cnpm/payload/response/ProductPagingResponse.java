package com.cnpm.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductPagingResponse {
    private List<ProductResponse> productPage;
    private int totalPages;
    private int pageNumber;
    private int pageSize;
    private int totalProducts;

}
