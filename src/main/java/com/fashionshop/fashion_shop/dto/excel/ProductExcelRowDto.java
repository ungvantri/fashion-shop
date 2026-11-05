package com.fashionshop.fashion_shop.dto.excel;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductExcelRowDto {
    private int rowIndex;
    private String productCode;
    private String productName;
    private Long categoryId;
    private Long brandId;
    private String season;
    private String description;
    private String size;
    private String color;
    private String sku;
    private String barcode;
    private BigDecimal costPrice;
    private BigDecimal retailPrice;
    private String imageUrl;
}
