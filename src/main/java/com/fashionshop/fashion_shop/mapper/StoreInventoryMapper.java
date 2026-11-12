package com.fashionshop.fashion_shop.mapper;

import com.fashionshop.fashion_shop.dto.response.StoreInventoryResponse;
import com.fashionshop.fashion_shop.entity.StoreInventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StoreInventoryMapper {

    @Mapping(target = "storeId", source = "store.id")
    @Mapping(target = "storeName", source = "store.name")
    @Mapping(target = "productVariantId", source = "productVariant.id")
    @Mapping(target = "productName", source = "productVariant.product.name")
    @Mapping(target = "sku", source = "productVariant.sku")
    @Mapping(target = "barcode", source = "productVariant.barcode")
    @Mapping(target = "size", source = "productVariant.size")
    @Mapping(target = "color", source = "productVariant.color")
    @Mapping(target = "retailPrice", source = "productVariant.retailPrice")
    @Mapping(target = "isLowStock", expression = "java(inventory.getQuantity() != null && inventory.getMinThreshold() != null && inventory.getQuantity() <= inventory.getMinThreshold())")
    StoreInventoryResponse toDto(StoreInventory inventory);

    List<StoreInventoryResponse> toListDto(List<StoreInventory> inventories);
}
