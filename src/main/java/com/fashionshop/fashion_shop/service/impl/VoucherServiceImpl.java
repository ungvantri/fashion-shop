package com.fashionshop.fashion_shop.service.impl;

import com.fashionshop.fashion_shop.dto.request.VoucherRequest;
import com.fashionshop.fashion_shop.dto.request.VoucherValidationRequest;
import com.fashionshop.fashion_shop.dto.response.PageResponse;
import com.fashionshop.fashion_shop.dto.response.VoucherResponse;
import com.fashionshop.fashion_shop.dto.response.VoucherValidationResponse;
import com.fashionshop.fashion_shop.entity.Voucher;
import com.fashionshop.fashion_shop.enums.DiscountType;
import com.fashionshop.fashion_shop.exception.ConflictException;
import com.fashionshop.fashion_shop.mapper.VoucherMapper;
import com.fashionshop.fashion_shop.repository.VoucherRepository;
import com.fashionshop.fashion_shop.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class VoucherServiceImpl implements VoucherService {

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private VoucherMapper voucherMapper;

    @Override
    public PageResponse<VoucherResponse> getVouchers(String search, Integer page, Integer size) {
        int pageIndex = (page != null && page > 0) ? page - 1 : 0;
        int pageSize = (size != null && size > 0) ? size : 20;
        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by("id").descending());

        String cleanSearch = (search != null && !search.isBlank()) ? search.trim() : null;
        Page<Voucher> pageData = voucherRepository.findVouchers(cleanSearch, pageable);
        List<VoucherResponse> items = voucherMapper.toListDto(pageData.getContent());

        return PageResponse.<VoucherResponse>builder()
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
    @Transactional
    public VoucherResponse createVoucher(VoucherRequest request) {
        String code = request.getCode().trim().toUpperCase();
        if (voucherRepository.existsByCode(code)) {
            throw new ConflictException("Mã voucher đã tồn tại: " + code);
        }

        Voucher voucher = voucherMapper.toEntity(request);
        voucher.setCode(code);
        voucher.setUsedCount(0);

        Voucher saved = voucherRepository.save(voucher);
        return voucherMapper.toDto(saved);
    }

    @Override
    public VoucherValidationResponse validateVoucher(VoucherValidationRequest request) {
        String code = request.getCode().trim().toUpperCase();
        BigDecimal orderAmount = request.getOrderAmount();

        Voucher voucher = voucherRepository.findByCode(code).orElse(null);
        if (voucher == null) {
            return VoucherValidationResponse.builder()
                    .isValid(false)
                    .message("Mã voucher không tồn tại")
                    .code(code)
                    .calculatedDiscountAmount(BigDecimal.ZERO)
                    .build();
        }

        LocalDate now = LocalDate.now();
        if (voucher.getValidFrom() != null && now.isBefore(voucher.getValidFrom())) {
            return VoucherValidationResponse.builder()
                    .isValid(false)
                    .message("Mã voucher chưa đến ngày áp dụng (bắt đầu từ " + voucher.getValidFrom() + ")")
                    .code(code)
                    .calculatedDiscountAmount(BigDecimal.ZERO)
                    .build();
        }

        if (voucher.getValidTo() != null && now.isAfter(voucher.getValidTo())) {
            return VoucherValidationResponse.builder()
                    .isValid(false)
                    .message("Mã voucher đã hết hạn sử dụng (hạn chót " + voucher.getValidTo() + ")")
                    .code(code)
                    .calculatedDiscountAmount(BigDecimal.ZERO)
                    .build();
        }

        if (voucher.getMaxUses() != null && voucher.getUsedCount() >= voucher.getMaxUses()) {
            return VoucherValidationResponse.builder()
                    .isValid(false)
                    .message("Mã voucher đã hết lượt sử dụng")
                    .code(code)
                    .calculatedDiscountAmount(BigDecimal.ZERO)
                    .build();
        }

        if (voucher.getMinOrderAmount() != null && orderAmount.compareTo(voucher.getMinOrderAmount()) < 0) {
            return VoucherValidationResponse.builder()
                    .isValid(false)
                    .message("Đơn hàng chưa đạt giá trị tối thiểu " + voucher.getMinOrderAmount() + " để áp dụng mã này")
                    .code(code)
                    .calculatedDiscountAmount(BigDecimal.ZERO)
                    .build();
        }

        BigDecimal discountAmount;
        if (voucher.getDiscountType() == DiscountType.PERCENT) {
            discountAmount = orderAmount.multiply(voucher.getDiscountValue())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else {
            discountAmount = voucher.getDiscountValue();
            if (discountAmount.compareTo(orderAmount) > 0) {
                discountAmount = orderAmount;
            }
        }

        return VoucherValidationResponse.builder()
                .isValid(true)
                .message("Áp dụng mã voucher thành công")
                .code(voucher.getCode())
                .discountType(voucher.getDiscountType())
                .discountValue(voucher.getDiscountValue())
                .calculatedDiscountAmount(discountAmount)
                .build();
    }
}
