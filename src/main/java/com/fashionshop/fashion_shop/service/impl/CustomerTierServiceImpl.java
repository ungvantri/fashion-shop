package com.fashionshop.fashion_shop.service.impl;

import com.fashionshop.fashion_shop.dto.request.CustomerTierRequest;
import com.fashionshop.fashion_shop.dto.response.CustomerTierResponse;
import com.fashionshop.fashion_shop.entity.CustomerTier;
import com.fashionshop.fashion_shop.exception.ConflictException;
import com.fashionshop.fashion_shop.mapper.CustomerTierMapper;
import com.fashionshop.fashion_shop.repository.CustomerTierRepository;
import com.fashionshop.fashion_shop.service.CustomerTierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerTierServiceImpl implements CustomerTierService {

    @Autowired
    private CustomerTierRepository customerTierRepository;

    @Autowired
    private CustomerTierMapper customerTierMapper;

    @Override
    @Cacheable(value = "customer_tiers", key = "'all'")
    public List<CustomerTierResponse> getAllTiers() {
        List<CustomerTier> tiers = customerTierRepository.findAll(Sort.by("minSpent").ascending());
        return customerTierMapper.toListDto(tiers);
    }

    @Override
    @Transactional
    @CacheEvict(value = "customer_tiers", allEntries = true)
    public CustomerTierResponse createTier(CustomerTierRequest request) {
        if (customerTierRepository.existsByTierName(request.getTierName())) {
            throw new ConflictException("Tên hạng thành viên đã tồn tại: " + request.getTierName());
        }

        CustomerTier entity = customerTierMapper.toEntity(request);
        CustomerTier saved = customerTierRepository.save(entity);
        return customerTierMapper.toDto(saved);
    }
}
