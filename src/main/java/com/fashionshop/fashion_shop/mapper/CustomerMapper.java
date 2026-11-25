package com.fashionshop.fashion_shop.mapper;

import com.fashionshop.fashion_shop.dto.request.CustomerRequest;
import com.fashionshop.fashion_shop.dto.response.CustomerResponse;
import com.fashionshop.fashion_shop.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "tier", ignore = true)
    @Mapping(target = "totalSpent", ignore = true)
    @Mapping(target = "currentPoints", ignore = true)
    Customer toEntity(CustomerRequest request);

    @Mapping(target = "tierId", source = "tier.id")
    @Mapping(target = "tierName", source = "tier.tierName")
    @Mapping(target = "discountPercent", source = "tier.discountPercent")
    CustomerResponse toDto(Customer customer);

    List<CustomerResponse> toListDto(List<Customer> customers);
}
