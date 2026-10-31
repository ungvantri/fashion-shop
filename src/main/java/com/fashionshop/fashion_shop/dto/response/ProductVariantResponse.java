package com.fashionshop.fashion_shop.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantResponse {
    private Long id;
    private Long productId;
    private String sku;
    private String barcode;
    private String size;
    private String color;
    private BigDecimal costPrice;
    private BigDecimal retailPrice;
    private String imageUrl;
}
