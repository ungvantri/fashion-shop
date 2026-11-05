package com.fashionshop.fashion_shop.mapper;

import com.fashionshop.fashion_shop.dto.request.ProductRequest;
import com.fashionshop.fashion_shop.dto.response.ProductResponse;
import com.fashionshop.fashion_shop.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProductVariantMapper.class})
public interface ProductMapper {
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "brandId", source = "brand.id")
    @Mapping(target = "brandName", source = "brand.name")
    ProductResponse toDto(Product product);

    List<ProductResponse> toListDto(List<Product> products);

    Product toEntity(ProductRequest productRequest);
}
