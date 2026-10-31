package com.fashionshop.fashion_shop.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreInventoryResponse {
    private Long id;
    private Long storeId;
    private String storeName;
    private Long productVariantId;
    private String productName;
    private String sku;
    private String barcode;
    private String size;
    private String color;
    private BigDecimal retailPrice;
    private Integer quantity;
    private Integer minThreshold;
    private Boolean isLowStock; // Cờ cảnh báo sắp hết hàng
}
