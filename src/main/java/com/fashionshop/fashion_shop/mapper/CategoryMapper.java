package com.fashionshop.fashion_shop.mapper;

import com.fashionshop.fashion_shop.dto.request.CategoryRequest;
import com.fashionshop.fashion_shop.dto.response.CategoryResponse;
import com.fashionshop.fashion_shop.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "parentId", source = "parent.id")
    @Mapping(target = "parentName", source = "parent.name")
    CategoryResponse toDto(Category category);

    List<CategoryResponse> toListDto(List<Category> categories);

    Category toEntity(CategoryRequest categoryRequest);
}
