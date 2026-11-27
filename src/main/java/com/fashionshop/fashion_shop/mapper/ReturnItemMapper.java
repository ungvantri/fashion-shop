package com.fashionshop.fashion_shop.mapper;

import com.fashionshop.fashion_shop.dto.response.ReturnItemResponse;
import com.fashionshop.fashion_shop.entity.ReturnItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReturnItemMapper {

    @Mapping(target = "productVariantId", source = "productVariant.id")
    @Mapping(target = "productName", source = "productVariant.product.name")
    @Mapping(target = "sku", source = "productVariant.sku")
    @Mapping(target = "barcode", source = "productVariant.barcode")
    @Mapping(target = "size", source = "productVariant.size")
    @Mapping(target = "color", source = "productVariant.color")
    @Mapping(target = "retailPrice", source = "productVariant.retailPrice")
    ReturnItemResponse toDto(ReturnItem returnItem);

    List<ReturnItemResponse> toListDto(List<ReturnItem> items);
}
