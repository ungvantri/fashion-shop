package com.fashionshop.fashion_shop.mapper;

import com.fashionshop.fashion_shop.dto.request.BrandRequest;
import com.fashionshop.fashion_shop.dto.response.BrandResponse;
import com.fashionshop.fashion_shop.entity.Brand;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BrandMapper {
    BrandResponse toDto(Brand brand);
    List<BrandResponse> toListDto(List<Brand> brands);
    Brand toEntity(BrandRequest brandRequest);
}
