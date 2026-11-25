package com.fashionshop.fashion_shop.service.impl;

import com.fashionshop.fashion_shop.dto.request.CustomerRequest;
import com.fashionshop.fashion_shop.dto.response.CustomerResponse;
import com.fashionshop.fashion_shop.dto.response.PageResponse;
import com.fashionshop.fashion_shop.entity.Customer;
import com.fashionshop.fashion_shop.entity.CustomerTier;
import com.fashionshop.fashion_shop.exception.ConflictException;
import com.fashionshop.fashion_shop.exception.ResourceNotFoundException;
import com.fashionshop.fashion_shop.mapper.CustomerMapper;
import com.fashionshop.fashion_shop.repository.CustomerRepository;
import com.fashionshop.fashion_shop.repository.CustomerTierRepository;
import com.fashionshop.fashion_shop.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerTierRepository customerTierRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public PageResponse<CustomerResponse> getCustomers(String search, Long tierId, Integer page, Integer size) {
        int pageIndex = (page != null && page > 0) ? page - 1 : 0;
        int pageSize = (size != null && size > 0) ? size : 20;
        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by("id").descending());

        String cleanSearch = (search != null && !search.isBlank()) ? search.trim() : null;
        Page<Customer> pageData = customerRepository.findCustomers(cleanSearch, tierId, pageable);
        List<CustomerResponse> items = customerMapper.toListDto(pageData.getContent());

        return PageResponse.<CustomerResponse>builder()
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
    @Cacheable(value = "customers", key = "'phone:' + #phone")
    public CustomerResponse getCustomerByPhone(String phone) {
        Customer customer = customerRepository.findByPhone(phone)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng với số điện thoại: " + phone));
        return customerMapper.toDto(customer);
    }

    @Override
    @Transactional
    @CacheEvict(value = "customers", key = "'phone:' + #request.phone")
    public CustomerResponse createCustomer(CustomerRequest request) {
        if (customerRepository.existsByPhone(request.getPhone())) {
            throw new ConflictException("Số điện thoại đã được đăng ký: " + request.getPhone());
        }

        CustomerTier tier;
        if (request.getTierId() != null) {
            tier = customerTierRepository.findById(request.getTierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hạng thành viên với ID: " + request.getTierId()));
        } else {
            tier = customerTierRepository.findFirstByOrderByMinSpentAsc()
                    .orElseThrow(() -> new ResourceNotFoundException("Hệ thống chưa cấu hình hạng thành viên"));
        }

        Customer customer = customerMapper.toEntity(request);
        customer.setTier(tier);
        customer.setTotalSpent(BigDecimal.ZERO);
        customer.setCurrentPoints(0);

        Customer saved = customerRepository.save(customer);
        return customerMapper.toDto(saved);
    }
}
