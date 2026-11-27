package com.fashionshop.fashion_shop.mapper;

import com.fashionshop.fashion_shop.dto.response.OrderPaymentResponse;
import com.fashionshop.fashion_shop.entity.OrderPayment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderPaymentMapper {

    OrderPaymentResponse toDto(OrderPayment orderPayment);

    List<OrderPaymentResponse> toListDto(List<OrderPayment> orderPayments);
}
