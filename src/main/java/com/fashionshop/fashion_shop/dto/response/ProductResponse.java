package com.fashionshop.fashion_shop.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private Long id;
    private String code;
    private String name;
    private Long categoryId;
    private String categoryName;
    private Long brandId;
    private String brandName;
    private String season;
    private Boolean isActive;
    private String description;
    private List<ProductVariantResponse> variants;
}
