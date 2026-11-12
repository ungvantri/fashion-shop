package com.fashionshop.fashion_shop.service.impl;

import com.fashionshop.fashion_shop.dto.request.StoreInventoryThresholdRequest;
import com.fashionshop.fashion_shop.dto.response.PageResponse;
import com.fashionshop.fashion_shop.dto.response.StoreInventoryResponse;
import com.fashionshop.fashion_shop.entity.ProductVariant;
import com.fashionshop.fashion_shop.entity.Store;
import com.fashionshop.fashion_shop.entity.StoreInventory;
import com.fashionshop.fashion_shop.exception.ResourceNotFoundException;
import com.fashionshop.fashion_shop.mapper.StoreInventoryMapper;
import com.fashionshop.fashion_shop.repository.ProductVariantRepository;
import com.fashionshop.fashion_shop.repository.StoreInventoryRepository;
import com.fashionshop.fashion_shop.repository.StoreRepository;
import com.fashionshop.fashion_shop.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private StoreInventoryRepository storeInventoryRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private StoreInventoryMapper storeInventoryMapper;

    @Override
    public PageResponse<StoreInventoryResponse> getInventories(Long storeId, String search, Integer page, Integer size) {
        int pageIndex = (page != null && page > 0) ? page - 1 : 0;
        int pageSize = (size != null && size > 0) ? size : 20;
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);

        String cleanSearch = (search != null && !search.isBlank()) ? search.trim() : null;
        Page<StoreInventory> pageData = storeInventoryRepository.findInventories(storeId, cleanSearch, pageable);

        List<StoreInventoryResponse> items = storeInventoryMapper.toListDto(pageData.getContent());

        return PageResponse.<StoreInventoryResponse>builder()
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
    public List<StoreInventoryResponse> getLowStockInventories(Long storeId) {
        if (storeId != null) {
            boolean storeExists = storeRepository.existsById(storeId);
            if (!storeExists) {
                throw new ResourceNotFoundException("Không tìm thấy chi nhánh với ID: " + storeId);
            }
        }
        List<StoreInventory> lowStockList = storeInventoryRepository.findLowStockInventories(storeId);
        return storeInventoryMapper.toListDto(lowStockList);
    }

    @Override
    @Transactional
    public StoreInventoryResponse updateThreshold(StoreInventoryThresholdRequest request) {
        boolean storeExists = storeRepository.existsById(request.getStoreId());
        boolean variantExists = productVariantRepository.existsById(request.getProductVariantId());

        if(!storeExists) {
            throw new ResourceNotFoundException("Không tìm thấy chi nhánh với ID: " + request.getStoreId());
        }

        if(!variantExists) {
            throw new ResourceNotFoundException("Không tìm thấy biến thể sản phẩm với ID: " + request.getProductVariantId());
        }

        StoreInventory inventory = storeInventoryRepository.findByStoreIdAndProductVariantId(request.getStoreId(), request.getProductVariantId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hàng tồn kho thỏa mãn yêu cầu"));

        inventory.setMinThreshold(request.getMinThreshold());

        return storeInventoryMapper.toDto(inventory);
    }
}
