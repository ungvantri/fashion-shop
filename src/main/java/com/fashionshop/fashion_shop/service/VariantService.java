package com.fashionshop.fashion_shop.service;

import com.fashionshop.fashion_shop.dto.response.ProductVariantResponse;

public interface VariantService {
    ProductVariantResponse getVariantByBarcode(String barcode);
}
