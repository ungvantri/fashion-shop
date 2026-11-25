package com.fashionshop.fashion_shop.mapper;

import com.fashionshop.fashion_shop.dto.request.CustomerTierRequest;
import com.fashionshop.fashion_shop.dto.response.CustomerTierResponse;
import com.fashionshop.fashion_shop.entity.CustomerTier;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerTierMapper {

    CustomerTier toEntity(CustomerTierRequest request);

    CustomerTierResponse toDto(CustomerTier entity);

    List<CustomerTierResponse> toListDto(List<CustomerTier> entities);
}
