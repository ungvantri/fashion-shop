package com.fashionshop.fashion_shop.mapper;

import com.fashionshop.fashion_shop.dto.response.StockTransferResponse;
import com.fashionshop.fashion_shop.entity.StockTransfer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {StockTransferItemMapper.class})
public interface StockTransferMapper {

    @Mapping(target = "fromStoreId", source = "fromStore.id")
    @Mapping(target = "fromStoreName", source = "fromStore.name")
    @Mapping(target = "toStoreId", source = "toStore.id")
    @Mapping(target = "toStoreName", source = "toStore.name")
    @Mapping(target = "sentByUserId", source = "sentByUser.id")
    @Mapping(target = "sentByUserName", source = "sentByUser.fullName")
    @Mapping(target = "receivedByUserId", source = "receivedByUser.id")
    @Mapping(target = "receivedByUserName", source = "receivedByUser.fullName")
    StockTransferResponse toDto(StockTransfer stockTransfer);

    List<StockTransferResponse> toListDto(List<StockTransfer> stockTransfers);
}
