package com.fashionshop.fashion_shop.mapper;

import com.fashionshop.fashion_shop.dto.request.UserRequest;
import com.fashionshop.fashion_shop.dto.response.UserResponse;
import com.fashionshop.fashion_shop.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "store", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    User toEntity(UserRequest request);

    @Mapping(target = "storeId", source = "store.id")
    @Mapping(target = "storeName", source = "store.name")
    UserResponse toDto(User user);

    List<UserResponse> toListDto(List<User> users);
}
