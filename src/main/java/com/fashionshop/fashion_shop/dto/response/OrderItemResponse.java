package com.fashionshop.fashion_shop.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponse {
    private Long id;
    private Long productVariantId;
    private String productName;
    private String sku;
    private String size;
    private String color;
    private Integer quantity;
    private BigDecimal retailPrice;
    private BigDecimal costPrice;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
}
