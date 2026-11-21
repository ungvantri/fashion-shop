package com.fashionshop.fashion_shop.mapper;

import com.fashionshop.fashion_shop.dto.response.StockTransferItemResponse;
import com.fashionshop.fashion_shop.entity.StockTransferItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StockTransferItemMapper {

    @Mapping(target = "productVariantId", source = "productVariant.id")
    @Mapping(target = "productName", source = "productVariant.product.name")
    @Mapping(target = "sku", source = "productVariant.sku")
    @Mapping(target = "barcode", source = "productVariant.barcode")
    @Mapping(target = "size", source = "productVariant.size")
    @Mapping(target = "color", source = "productVariant.color")
    StockTransferItemResponse toDto(StockTransferItem item);

    List<StockTransferItemResponse> toListDto(List<StockTransferItem> items);
}
