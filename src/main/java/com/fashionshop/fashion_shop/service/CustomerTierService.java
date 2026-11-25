package com.fashionshop.fashion_shop.service;

import com.fashionshop.fashion_shop.dto.request.CustomerTierRequest;
import com.fashionshop.fashion_shop.dto.response.CustomerTierResponse;

import java.util.List;

public interface CustomerTierService {

    List<CustomerTierResponse> getAllTiers();

    CustomerTierResponse createTier(CustomerTierRequest request);
}
