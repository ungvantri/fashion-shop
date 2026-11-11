package com.fashionshop.fashion_shop.service.impl;

import com.fashionshop.fashion_shop.document.ProductDocument;
import com.fashionshop.fashion_shop.dto.response.PageResponse;
import com.fashionshop.fashion_shop.dto.response.ProductResponse;
import com.fashionshop.fashion_shop.dto.response.ProductVariantResponse;
import com.fashionshop.fashion_shop.repository.ProductElasticsearchRepository;
import com.fashionshop.fashion_shop.service.ProductSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Service
public class ProductSearchServiceImpl implements ProductSearchService {

    @Autowired
    private ProductElasticsearchRepository productElasticsearchRepository;

    @Override
    public PageResponse<ProductResponse> searchProducts(
            String keyword,
            Long categoryId,
            Long brandId,
            String color,
            String size,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            int pageIndex,
            Integer pageSize
    ) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by("id").descending());
        Page<ProductDocument> pageResult;

        boolean hasKeyword = (keyword != null && !keyword.isBlank());
        boolean hasCategory = (categoryId != null);
        boolean hasColorAndSize = (color != null && !color.isBlank() && size != null && !size.isBlank());
        boolean hasPrice = (minPrice != null && maxPrice != null);

        // Điều hướng gọi trực tiếp các phương thức @Query trong Repository
        if (hasKeyword && hasCategory) {
            pageResult = productElasticsearchRepository.searchByKeywordAndCategory(keyword.trim(), categoryId, pageable);
        } else if (hasKeyword) {
            pageResult = productElasticsearchRepository.searchByKeyword(keyword.trim(), pageable);
        } else if (hasCategory) {
            pageResult = productElasticsearchRepository.findByCategoryId(categoryId, pageable);
        } else if (hasColorAndSize) {
            pageResult = productElasticsearchRepository.findByColorAndSize(color.trim(), size.trim(), pageable);
        } else if (hasPrice) {
            pageResult = productElasticsearchRepository.findByPriceRange(minPrice, maxPrice, pageable);
        } else {
            pageResult = productElasticsearchRepository.findAll(pageable);
        }

        List<ProductResponse> items = pageResult.getContent().stream()
                .map(this::toProductResponse)
                .toList();

        return PageResponse.<ProductResponse>builder()
                .items(items)
                .currentPage(pageResult.getNumber() + 1)
                .pageSize(pageResult.getSize())
                .totalItems(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .hasNext(pageResult.hasNext())
                .hasPrevious(pageResult.hasPrevious())
                .build();
    }

    private ProductResponse toProductResponse(ProductDocument doc) {
        if (doc == null) return null;
        return ProductResponse.builder()
                .id(doc.getId())
                .code(doc.getCode())
                .name(doc.getName())
                .season(doc.getSeason())
                .isActive(doc.getIsActive())
                .description(doc.getDescription())
                .categoryId(doc.getCategoryId())
                .categoryName(doc.getCategoryName())
                .brandId(doc.getBrandId())
                .brandName(doc.getBrandName())
                .variants(doc.getVariants() != null ? doc.getVariants().stream()
                        .map(v -> ProductVariantResponse.builder()
                                .id(v.getId())
                                .productId(doc.getId())
                                .sku(v.getSku())
                                .barcode(v.getBarcode())
                                .size(v.getSize())
                                .color(v.getColor())
                                .costPrice(v.getCostPrice())
                                .retailPrice(v.getRetailPrice())
                                .imageUrl(v.getImageUrl())
                                .build())
                        .toList() : Collections.emptyList())
                .build();
    }
}
