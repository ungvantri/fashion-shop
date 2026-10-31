package com.fashionshop.fashion_shop.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockTransferItemResponse {
    private Long id;
    private Long productVariantId;
    private String productName;
    private String sku;
    private String barcode;
    private String size;
    private String color;
    private Integer quantity;
}
