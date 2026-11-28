package com.fashionshop.fashion_shop.service.impl;

import com.fashionshop.fashion_shop.dto.request.StockTransferItemRequest;
import com.fashionshop.fashion_shop.dto.request.StockTransferRequest;
import com.fashionshop.fashion_shop.dto.request.StockTransferStatusRequest;
import com.fashionshop.fashion_shop.dto.response.PageResponse;
import com.fashionshop.fashion_shop.dto.response.StockTransferResponse;
import com.fashionshop.fashion_shop.entity.*;
import com.fashionshop.fashion_shop.enums.TransferStatus;
import com.fashionshop.fashion_shop.exception.ConflictException;
import com.fashionshop.fashion_shop.exception.ResourceNotFoundException;
import com.fashionshop.fashion_shop.mapper.StockTransferMapper;
import com.fashionshop.fashion_shop.repository.*;
import com.fashionshop.fashion_shop.service.StockTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StockTransferServiceImpl implements StockTransferService {

    @Autowired
    private StockTransferRepository stockTransferRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private StoreInventoryRepository storeInventoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StockTransferMapper stockTransferMapper;

    @Override
    public PageResponse<StockTransferResponse> getStockTransfers(
            Long fromStoreId,
            Long toStoreId,
            TransferStatus status,
            Integer page,
            Integer size
    ) {
        int pageIndex = (page != null && page > 0) ? page - 1 : 0;
        int pageSize = (size != null && size > 0) ? size : 20;
        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by("id").descending());

        Page<StockTransfer> pageData = stockTransferRepository.findStockTransfers(fromStoreId, toStoreId, status, pageable);
        List<StockTransferResponse> items = stockTransferMapper.toListDto(pageData.getContent());

        return PageResponse.<StockTransferResponse>builder()
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
    @Cacheable(value = "stock_transfers", key = "'id:' + #id")
    public StockTransferResponse getStockTransferById(Long id) {
        StockTransfer stockTransfer = stockTransferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu chuyển kho với ID: " + id));
        return stockTransferMapper.toDto(stockTransfer);
    }

    @Override
    @Transactional
    @CacheEvict(value = "inventories", allEntries = true)
    public StockTransferResponse createStockTransfer(StockTransferRequest request) {
        if (request.getFromStoreId().equals(request.getToStoreId())) {
            throw new ConflictException("Chi nhánh gửi và chi nhánh nhận không được trùng nhau");
        }

        Store fromStore = storeRepository.findById(request.getFromStoreId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chi nhánh gửi với ID: " + request.getFromStoreId()));

        Store toStore = storeRepository.findById(request.getToStoreId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chi nhánh nhận với ID: " + request.getToStoreId()));

        User sentByUser = null;
        if (request.getSentByUserId() != null) {
            sentByUser = userRepository.findById(request.getSentByUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên tạo phiếu với ID: " + request.getSentByUserId()));
        }

        String code = generateTransferCode();

        StockTransfer stockTransfer = StockTransfer.builder()
                .code(code)
                .fromStore(fromStore)
                .toStore(toStore)
                .sentByUser(sentByUser)
                .status(TransferStatus.SHIPPED)
                .shippedAt(LocalDateTime.now())
                .note(request.getNote())
                .items(new ArrayList<>())
                .build();

        List<Long> variantIds = request.getItems().stream()
                .map(StockTransferItemRequest::getProductVariantId)
                .distinct()
                .toList();

        Map<Long, ProductVariant> variantMap = productVariantRepository.findAllById(variantIds).stream()
                .collect(Collectors.toMap(ProductVariant::getId, Function.identity()));

        List<Long> missingVariantIds = variantIds.stream()
                .filter(vid -> !variantMap.containsKey(vid))
                .toList();
        if (!missingVariantIds.isEmpty()) {
            throw new ResourceNotFoundException("Không tìm thấy biến thể sản phẩm với ID: " + missingVariantIds);
        }

        Map<Long, StoreInventory> inventoryMap = storeInventoryRepository
                .findByStoreIdAndVariantIds(fromStore.getId(), variantIds).stream()
                .collect(Collectors.toMap(si -> si.getProductVariant().getId(), Function.identity()));

        List<String> noInventorySkus = variantIds.stream()
                .filter(vid -> !inventoryMap.containsKey(vid))
                .map(vid -> variantMap.get(vid).getSku())
                .toList();
        if (!noInventorySkus.isEmpty()) {
            throw new ConflictException("Các sản phẩm chưa có tồn kho tại chi nhánh gửi: " + noInventorySkus);
        }

        for (StockTransferItemRequest itemReq : request.getItems()) {
            ProductVariant variant = variantMap.get(itemReq.getProductVariantId());
            StoreInventory sourceInventory = inventoryMap.get(variant.getId());

            if (sourceInventory.getQuantity() < itemReq.getQuantity()) {
                throw new ConflictException("Tồn kho tại " + fromStore.getName() + " không đủ để chuyển. Sản phẩm "
                        + variant.getSku() + " chỉ còn " + sourceInventory.getQuantity()
                        + " nhưng yêu cầu chuyển " + itemReq.getQuantity());
            }

            sourceInventory.setQuantity(sourceInventory.getQuantity() - itemReq.getQuantity());
            stockTransfer.getItems().add(StockTransferItem.builder()
                    .transfer(stockTransfer)
                    .productVariant(variant)
                    .quantity(itemReq.getQuantity())
                    .build());
        }

        StockTransfer saved = stockTransferRepository.save(stockTransfer);
        return stockTransferMapper.toDto(saved);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "stock_transfers", key = "'id:' + #id"),
            @CacheEvict(value = "inventories", allEntries = true)
    })
    public StockTransferResponse updateStatus(Long id, StockTransferStatusRequest request) {
        StockTransfer stockTransfer = stockTransferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu chuyển kho với ID: " + id));

        TransferStatus currentStatus = stockTransfer.getStatus();
        TransferStatus newStatus = request.getStatus();

        if (currentStatus == TransferStatus.RECEIVED) {
            throw new ConflictException("Phiếu chuyển kho đã hoàn thành (RECEIVED), không thể thay đổi trạng thái");
        }
        if (currentStatus == TransferStatus.REJECTED) {
            throw new ConflictException("Phiếu chuyển kho đã bị từ chối (REJECTED), không thể thay đổi trạng thái");
        }
        if (newStatus == TransferStatus.DRAFT) {
            throw new ConflictException("Không thể chuyển trạng thái phiếu về DRAFT");
        }

        if (newStatus == TransferStatus.RECEIVED) {
            User receivedByUser = null;
            if (request.getReceivedByUserId() != null) {
                receivedByUser = userRepository.findById(request.getReceivedByUserId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Không tìm thấy nhân viên nhận hàng với ID: " + request.getReceivedByUserId()));
            }

            addStock(stockTransfer.getToStore(), stockTransfer.getItems());

            stockTransfer.setReceivedAt(LocalDateTime.now());
            if (receivedByUser != null) {
                stockTransfer.setReceivedByUser(receivedByUser);
            }
        } else if (newStatus == TransferStatus.REJECTED) {
            addStock(stockTransfer.getFromStore(), stockTransfer.getItems());
        }

        stockTransfer.setStatus(newStatus);
        return stockTransferMapper.toDto(stockTransfer);
    }

    private String generateTransferCode() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));
        String randomSuffix = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return "TRF-" + timestamp + "-" + randomSuffix;
    }

    private void addStock(Store store, List<StockTransferItem> items) {
        List<Long> variantIds = items.stream()
                .map(item -> item.getProductVariant().getId())
                .distinct()
                .toList();

        Map<Long, StoreInventory> inventoryMap = storeInventoryRepository
                .findByStoreIdAndVariantIds(store.getId(), variantIds).stream()
                .collect(Collectors.toMap(si -> si.getProductVariant().getId(), Function.identity()));

        List<StoreInventory> newInventories = new ArrayList<>();

        for (StockTransferItem item : items) {
            ProductVariant variant = item.getProductVariant();

            StoreInventory inventory = inventoryMap.get(variant.getId());
            if (inventory == null) {
                inventory = StoreInventory.builder()
                        .store(store)
                        .productVariant(variant)
                        .quantity(0)
                        .minThreshold(5)
                        .build();
                inventoryMap.put(variant.getId(), inventory);
                newInventories.add(inventory);
            }

            inventory.setQuantity(inventory.getQuantity() + item.getQuantity());
        }

        storeInventoryRepository.saveAll(newInventories);
    }
}
