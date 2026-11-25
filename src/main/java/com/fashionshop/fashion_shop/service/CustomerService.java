package com.fashionshop.fashion_shop.service;

import com.fashionshop.fashion_shop.dto.request.CustomerRequest;
import com.fashionshop.fashion_shop.dto.response.CustomerResponse;
import com.fashionshop.fashion_shop.dto.response.PageResponse;

public interface CustomerService {

    PageResponse<CustomerResponse> getCustomers(String search, Long tierId, Integer page, Integer size);

    CustomerResponse getCustomerByPhone(String phone);

    CustomerResponse createCustomer(CustomerRequest request);
}
