package com.fashionshop.fashion_shop.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BestSellerProductResponse {
    private Long productId;
    private String productCode;
    private String productName;
    private Long totalSoldQuantity;
    private BigDecimal totalRevenue;
}
