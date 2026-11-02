package com.fashionshop.fashion_shop.mapper;

import com.fashionshop.fashion_shop.dto.request.StoreRequest;
import com.fashionshop.fashion_shop.dto.response.StoreResponse;
import com.fashionshop.fashion_shop.entity.Store;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StoreMapper {
    StoreResponse toDto(Store store);

    Store toEntity(StoreRequest storeRequest);

    List<StoreResponse> toListDto(List<Store> stores);
}
