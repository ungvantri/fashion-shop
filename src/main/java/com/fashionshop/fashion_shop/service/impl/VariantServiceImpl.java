package com.fashionshop.fashion_shop.service.impl;

import com.fashionshop.fashion_shop.dto.response.ProductVariantResponse;
import com.fashionshop.fashion_shop.entity.ProductVariant;
import com.fashionshop.fashion_shop.exception.ResourceNotFoundException;
import com.fashionshop.fashion_shop.mapper.ProductVariantMapper;
import com.fashionshop.fashion_shop.repository.ProductVariantRepository;
import com.fashionshop.fashion_shop.service.VariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VariantServiceImpl implements VariantService {
    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private ProductVariantMapper productVariantMapper;
    @Override
    public ProductVariantResponse getVariantByBarcode(String barcode) {
        ProductVariant productVariant = productVariantRepository.findByBarcode(barcode).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy biến thể với mã vạch: " + barcode));
        return productVariantMapper.toDto(productVariant);
    }
}
