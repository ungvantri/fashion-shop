package com.fashionshop.fashion_shop.service.impl;

import com.fashionshop.fashion_shop.dto.request.UserRequest;
import com.fashionshop.fashion_shop.dto.request.UserUpdateRequest;
import com.fashionshop.fashion_shop.dto.response.PageResponse;
import com.fashionshop.fashion_shop.dto.response.UserResponse;
import com.fashionshop.fashion_shop.entity.Store;
import com.fashionshop.fashion_shop.entity.User;
import com.fashionshop.fashion_shop.enums.RoleType;
import com.fashionshop.fashion_shop.exception.ConflictException;
import com.fashionshop.fashion_shop.exception.ResourceNotFoundException;
import com.fashionshop.fashion_shop.mapper.UserMapper;
import com.fashionshop.fashion_shop.repository.StoreRepository;
import com.fashionshop.fashion_shop.repository.UserRepository;
import com.fashionshop.fashion_shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public PageResponse<UserResponse> getUsers(Long storeId, RoleType role, Integer page, Integer size) {
        int pageIndex = (page != null && page > 0) ? page - 1 : 0;
        int pageSize = (size != null && size > 0) ? size : 20;
        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by("id").descending());

        Page<User> pageData = userRepository.findUsers(storeId, role, pageable);
        List<UserResponse> items = userMapper.toListDto(pageData.getContent());

        return PageResponse.<UserResponse>builder()
                .items(items)
                .currentPage(pageData.getNumber() + 1)
                .pageSize(pageData.getSize())
                .totalItems(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .hasNext(pageData.hasNext())
                .hasPrevious(pageData.hasPrevious())
                .build();
    }

    @Override
    @Cacheable(value = "users", key = "'id:' + #id")
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên với ID: " + id));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserResponse createUser(UserRequest request) {
        String username = request.getUsername().trim().toLowerCase();
        if (userRepository.existsByUsername(username)) {
            throw new ConflictException("Tên đăng nhập đã tồn tại: " + username);
        }

        Store store = null;
        if (request.getStoreId() != null) {
            store = storeRepository.findById(request.getStoreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chi nhánh với ID: " + request.getStoreId()));
        }

        User user = userMapper.toEntity(request);
        user.setUsername(username);
        user.setStore(store);
        // Lưu tạm hash cơ bản (hoặc plain-text nếu chưa tích hợp BCryptPasswordEncoder)
        user.setPasswordHash(request.getPassword());

        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", key = "'id:' + #id")
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên với ID: " + id));

        if (request.getStoreId() != null) {
            Store store = storeRepository.findById(request.getStoreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chi nhánh với ID: " + request.getStoreId()));
            user.setStore(store);
        }

        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }

        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            user.setFullName(request.getFullName().trim());
        }

        if (request.getPhone() != null) {
            user.setPhone(request.getPhone().trim());
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPasswordHash(request.getPassword());
        }

        if (request.getCommissionRate() != null) {
            user.setCommissionRate(request.getCommissionRate());
        }

        if (request.getIsActive() != null) {
            user.setIsActive(request.getIsActive());
        }

        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }
}
