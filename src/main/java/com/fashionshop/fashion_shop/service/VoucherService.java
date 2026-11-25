package com.fashionshop.fashion_shop.service;

import com.fashionshop.fashion_shop.dto.request.VoucherRequest;
import com.fashionshop.fashion_shop.dto.request.VoucherValidationRequest;
import com.fashionshop.fashion_shop.dto.response.PageResponse;
import com.fashionshop.fashion_shop.dto.response.VoucherResponse;
import com.fashionshop.fashion_shop.dto.response.VoucherValidationResponse;

public interface VoucherService {

    PageResponse<VoucherResponse> getVouchers(String search, Integer page, Integer size);

    VoucherResponse createVoucher(VoucherRequest request);

    VoucherValidationResponse validateVoucher(VoucherValidationRequest request);
}
