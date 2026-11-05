package com.fashionshop.fashion_shop.mapper;


import com.fashionshop.fashion_shop.dto.request.ProductVariantRequest;
import com.fashionshop.fashion_shop.dto.response.ProductVariantResponse;
import com.fashionshop.fashion_shop.entity.ProductVariant;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductVariantMapper {
    ProductVariantResponse toDto(ProductVariant productVariant);

    List<ProductVariantResponse> toListDto(List<ProductVariant> productVariants);

    ProductVariant toEntity(ProductVariantRequest productVariantRequest);

    List<ProductVariant> toListEntity(List<ProductVariantRequest> productVariantRequests);
}
