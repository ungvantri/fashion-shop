package com.fashionshop.fashion_shop.service;

import com.fashionshop.fashion_shop.dto.request.StoreInventoryThresholdRequest;
import com.fashionshop.fashion_shop.dto.response.PageResponse;
import com.fashionshop.fashion_shop.dto.response.StoreInventoryResponse;

import java.util.List;

public interface InventoryService {
    PageResponse<StoreInventoryResponse> getInventories(Long storeId, String search, Integer page, Integer size);

    List<StoreInventoryResponse> getLowStockInventories(Long storeId);

    StoreInventoryResponse updateThreshold(StoreInventoryThresholdRequest request);
}
