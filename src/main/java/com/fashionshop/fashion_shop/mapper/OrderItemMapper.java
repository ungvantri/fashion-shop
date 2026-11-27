package com.fashionshop.fashion_shop.mapper;

import com.fashionshop.fashion_shop.dto.response.OrderItemResponse;
import com.fashionshop.fashion_shop.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "productVariantId", source = "productVariant.id")
    @Mapping(target = "productName", source = "productVariant.product.name")
    @Mapping(target = "sku", source = "productVariant.sku")
    @Mapping(target = "size", source = "productVariant.size")
    @Mapping(target = "color", source = "productVariant.color")
    OrderItemResponse toDto(OrderItem orderItem);

    List<OrderItemResponse> toListDto(List<OrderItem> orderItems);
}
