package com.fashionshop.fashion_shop.controller;

import com.fashionshop.fashion_shop.dto.request.UserRequest;
import com.fashionshop.fashion_shop.dto.request.UserUpdateRequest;
import com.fashionshop.fashion_shop.dto.response.ApiResponse;
import com.fashionshop.fashion_shop.dto.response.PageResponse;
import com.fashionshop.fashion_shop.dto.response.UserResponse;
import com.fashionshop.fashion_shop.enums.RoleType;
import com.fashionshop.fashion_shop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> getUsers(
            @RequestParam(required = false) Long storeId,
            @RequestParam(required = false) RoleType role,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size
    ) {
        PageResponse<UserResponse> pageResponse = userService.getUsers(storeId, role, page, size);
        ApiResponse<PageResponse<UserResponse>> result = ApiResponse.<PageResponse<UserResponse>>builder()
                .success(true)
                .message("Lấy danh sách nhân viên thành công")
                .data(pageResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        UserResponse response = userService.getUserById(id);
        ApiResponse<UserResponse> result = ApiResponse.<UserResponse>builder()
                .success(true)
                .message("Lấy thông tin nhân viên thành công")
                .data(response)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody UserRequest request) {
        UserResponse response = userService.createUser(request);
        ApiResponse<UserResponse> result = ApiResponse.<UserResponse>builder()
                .success(true)
                .message("Tạo tài khoản nhân viên mới thành công")
                .data(response)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request
    ) {
        UserResponse response = userService.updateUser(id, request);
        ApiResponse<UserResponse> result = ApiResponse.<UserResponse>builder()
                .success(true)
                .message("Cập nhật thông tin nhân viên thành công")
                .data(response)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
