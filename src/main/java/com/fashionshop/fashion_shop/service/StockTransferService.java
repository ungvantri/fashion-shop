package com.fashionshop.fashion_shop.service;

import com.fashionshop.fashion_shop.dto.request.StockTransferRequest;
import com.fashionshop.fashion_shop.dto.request.StockTransferStatusRequest;
import com.fashionshop.fashion_shop.dto.response.PageResponse;
import com.fashionshop.fashion_shop.dto.response.StockTransferResponse;
import com.fashionshop.fashion_shop.enums.TransferStatus;

public interface StockTransferService {

    PageResponse<StockTransferResponse> getStockTransfers(
            Long fromStoreId,
            Long toStoreId,
            TransferStatus status,
            Integer page,
            Integer size
    );

    StockTransferResponse createStockTransfer(StockTransferRequest request);

    StockTransferResponse updateStatus(Long id, StockTransferStatusRequest request);

    StockTransferResponse getStockTransferById(Long id);
}
