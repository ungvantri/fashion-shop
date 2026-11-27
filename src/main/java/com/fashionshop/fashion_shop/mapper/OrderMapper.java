package com.fashionshop.fashion_shop.mapper;

import com.fashionshop.fashion_shop.dto.response.OrderResponse;
import com.fashionshop.fashion_shop.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, OrderPaymentMapper.class})
public interface OrderMapper {

    @Mapping(target = "storeId", source = "store.id")
    @Mapping(target = "storeName", source = "store.name")
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerName", source = "customer.fullName")
    @Mapping(target = "customerPhone", source = "customer.phone")
    @Mapping(target = "shiftId", source = "shift.id")
    @Mapping(target = "cashierId", source = "cashier.id")
    @Mapping(target = "cashierName", source = "cashier.fullName")
    @Mapping(target = "sellerId", source = "seller.id")
    @Mapping(target = "sellerName", source = "seller.fullName")
    OrderResponse toDto(Order order);

    List<OrderResponse> toListDto(List<Order> orders);
}
