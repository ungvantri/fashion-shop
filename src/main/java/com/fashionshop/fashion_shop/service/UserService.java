package com.fashionshop.fashion_shop.service;

import com.fashionshop.fashion_shop.dto.request.UserRequest;
import com.fashionshop.fashion_shop.dto.request.UserUpdateRequest;
import com.fashionshop.fashion_shop.dto.response.PageResponse;
import com.fashionshop.fashion_shop.dto.response.UserResponse;
import com.fashionshop.fashion_shop.enums.RoleType;

public interface UserService {

    PageResponse<UserResponse> getUsers(Long storeId, RoleType role, Integer page, Integer size);

    UserResponse getUserById(Long id);

    UserResponse createUser(UserRequest request);

    UserResponse updateUser(Long id, UserUpdateRequest request);
}
