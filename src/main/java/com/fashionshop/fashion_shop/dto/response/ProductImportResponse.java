package com.fashionshop.fashion_shop.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImportResponse {
    private int totalRows;
    private int totalProducts;
    private int totalVariants;
    private List<String> errors;
}
