package com.fashionshop.fashion_shop.mapper;

import com.fashionshop.fashion_shop.dto.request.VoucherRequest;
import com.fashionshop.fashion_shop.dto.response.VoucherResponse;
import com.fashionshop.fashion_shop.entity.Voucher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring")
public interface VoucherMapper {

    @Mapping(target = "usedCount", ignore = true)
    Voucher toEntity(VoucherRequest request);

    @Mapping(target = "isValid", expression = "java(checkIsValid(voucher))")
    VoucherResponse toDto(Voucher voucher);

    List<VoucherResponse> toListDto(List<Voucher> vouchers);

    default Boolean checkIsValid(Voucher voucher) {
        if (voucher == null) return false;
        LocalDate now = LocalDate.now();
        if (voucher.getValidFrom() != null && now.isBefore(voucher.getValidFrom())) return false;
        if (voucher.getValidTo() != null && now.isAfter(voucher.getValidTo())) return false;
        if (voucher.getMaxUses() != null && voucher.getUsedCount() != null && voucher.getUsedCount() >= voucher.getMaxUses()) return false;
        return true;
    }
}
