package com.fashionshop.fashion_shop.service.impl;

import com.fashionshop.fashion_shop.dto.request.StoreRequest;
import com.fashionshop.fashion_shop.dto.response.StoreResponse;
import com.fashionshop.fashion_shop.entity.Store;
import com.fashionshop.fashion_shop.exception.ResourceNotFoundException;
import com.fashionshop.fashion_shop.mapper.StoreMapper;
import com.fashionshop.fashion_shop.repository.StoreRepository;
import com.fashionshop.fashion_shop.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StoreServiceImpl implements StoreService {
    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreMapper storeMapper;

    @Override
    public List<StoreResponse> getAllStores() {
        List<Store> stores = storeRepository.findAll();
        return storeMapper.toListDto(stores);
    }

    @Override
    @Transactional
    public StoreResponse createStore(StoreRequest storeRequest) {
        Store store = storeMapper.toEntity(storeRequest);
        Store saved = storeRepository.save(store);
        return storeMapper.toDto(saved);
    }

    @Override
    @Transactional
    public StoreResponse updateStore(Long id, StoreRequest storeRequest) {
        Store store = storeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chi nhánh có id: " + id));
        store.setCode(storeRequest.getCode());
        store.setName(storeRequest.getName());
        store.setPhone(storeRequest.getPhone());
        store.setAddress(storeRequest.getAddress());
        store.setIsActive(storeRequest.getIsActive());
        storeRepository.save(store);
        return storeMapper.toDto(store);
    }

    @Override
    public StoreResponse getStoreById(Long id) {
        Store store = storeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chi nhánh có id: " + id));
        return storeMapper.toDto(store);
    }
}
