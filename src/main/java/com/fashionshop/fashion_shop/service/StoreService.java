package com.fashionshop.fashion_shop.service;

import com.fashionshop.fashion_shop.dto.request.StoreRequest;
import com.fashionshop.fashion_shop.dto.response.StoreResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface StoreService {
    List<StoreResponse> getAllStores();

    StoreResponse createStore(@Valid StoreRequest storeRequest);

    StoreResponse updateStore(Long id, @Valid StoreRequest storeRequest);

    StoreResponse getStoreById(Long id);
}
