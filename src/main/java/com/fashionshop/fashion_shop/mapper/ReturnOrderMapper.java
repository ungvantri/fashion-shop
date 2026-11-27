package com.fashionshop.fashion_shop.mapper;

import com.fashionshop.fashion_shop.dto.response.ReturnOrderResponse;
import com.fashionshop.fashion_shop.entity.ReturnOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ReturnItemMapper.class})
public interface ReturnOrderMapper {

    @Mapping(target = "originalOrderId", source = "originalOrder.id")
    @Mapping(target = "originalOrderCode", source = "originalOrder.orderCode")
    @Mapping(target = "storeId", source = "store.id")
    @Mapping(target = "storeName", source = "store.name")
    @Mapping(target = "createdById", source = "createdBy.id")
    @Mapping(target = "createdByName", source = "createdBy.fullName")
    ReturnOrderResponse toDto(ReturnOrder returnOrder);

    List<ReturnOrderResponse> toListDto(List<ReturnOrder> returnOrders);
}
