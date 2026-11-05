package com.fashionshop.fashion_shop.service.impl;

import com.fashionshop.fashion_shop.dto.excel.ProductExcelRowDto;
import com.fashionshop.fashion_shop.dto.request.ProductRequest;
import com.fashionshop.fashion_shop.dto.request.ProductVariantRequest;
import com.fashionshop.fashion_shop.dto.response.PageResponse;
import com.fashionshop.fashion_shop.dto.response.ProductImportResponse;
import com.fashionshop.fashion_shop.dto.response.ProductResponse;
import com.fashionshop.fashion_shop.entity.Brand;
import com.fashionshop.fashion_shop.entity.Category;
import com.fashionshop.fashion_shop.entity.Product;
import com.fashionshop.fashion_shop.entity.ProductVariant;
import com.fashionshop.fashion_shop.exception.ResourceNotFoundException;
import com.fashionshop.fashion_shop.mapper.ProductMapper;
import com.fashionshop.fashion_shop.mapper.ProductVariantMapper;
import com.fashionshop.fashion_shop.repository.BrandRepository;
import com.fashionshop.fashion_shop.repository.CategoryRepository;
import com.fashionshop.fashion_shop.repository.ProductRepository;
import com.fashionshop.fashion_shop.repository.ProductVariantRepository;
import com.fashionshop.fashion_shop.service.FileStorageService;
import com.fashionshop.fashion_shop.service.ProductService;
import com.fashionshop.fashion_shop.util.ExcelHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductVariantMapper productVariantMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public PageResponse<ProductResponse> getProducts(String search, Long categoryId, String season, Boolean isActive, Integer page, Integer size) {
        int pageIndex = (page != null && page > 0) ? page - 1 : 0;
        Pageable pageable = PageRequest.of(pageIndex, size);
        Page<Product> pageProduct = productRepository.findByFilters(search, categoryId, season, isActive, pageable);
        List<Product> products = pageProduct.getContent();
        List<ProductResponse> result = productMapper.toListDto(products);
        return PageResponse.<ProductResponse>builder()
                .items(result)
                .currentPage(pageProduct.getNumber() + 1)
                .pageSize(pageProduct.getSize())
                .totalItems(pageProduct.getTotalElements())
                .totalPages(pageProduct.getTotalPages())
                .hasNext(pageProduct.hasNext())
                .hasPrevious(pageProduct.hasPrevious())
                .build();
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = productMapper.toEntity(productRequest);
        Category category = categoryRepository.findById(productRequest.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục có id: " + productRequest.getCategoryId()));
        Brand brand = null;
        if (productRequest.getBrandId() != null) {
            brand = brandRepository.findById(productRequest.getBrandId()).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhãn hiệu có id: " + productRequest.getBrandId()));
        }
        List<ProductVariant> productVariants = productVariantMapper.toListEntity(productRequest.getVariants());
        product.setCategory(category);
        product.setBrand(brand);
        product.setVariants(productVariants);
        if (productVariants != null) {
            for (ProductVariant variant : productVariants) {
                variant.setProduct(product);
            }
        }
        Product saved = productRepository.save(product);
        return productMapper.toDto(saved);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm có id: " + id));
        return productMapper.toDto(product);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm có id: " + id));
        Category category = categoryRepository.findById(productRequest.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục có id: " + productRequest.getCategoryId()));
        Brand brand = null;
        if (productRequest.getBrandId() != null) {
            brand = brandRepository.findById(productRequest.getBrandId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhãn hiệu có id: " + productRequest.getBrandId()));
        }
        product.setCode(productRequest.getCode());
        product.setName(productRequest.getName());
        product.setSeason(productRequest.getSeason());
        product.setIsActive(productRequest.getIsActive());
        product.setDescription(productRequest.getDescription());
        if (product.getVariants() == null) {
            product.setVariants(new ArrayList<>());
        }
        Set<Long> requestVariantIds = productRequest.getVariants().stream()
                .map(ProductVariantRequest::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        product.getVariants().removeIf(v -> v.getId() != null && !requestVariantIds.contains(v.getId()));

        for (ProductVariantRequest vReq : productRequest.getVariants()) {
            if (vReq.getId() != null) {
                product.getVariants().stream()
                        .filter(v -> v.getId().equals(vReq.getId()))
                        .findFirst()
                        .ifPresent(existingVariant -> {
                            existingVariant.setSku(vReq.getSku());
                            existingVariant.setBarcode(vReq.getBarcode());
                            existingVariant.setSize(vReq.getSize());
                            existingVariant.setColor(vReq.getColor());
                            existingVariant.setCostPrice(vReq.getCostPrice());
                            existingVariant.setRetailPrice(vReq.getRetailPrice());
                            existingVariant.setImageUrl(vReq.getImageUrl());
                        });
            } else {
                ProductVariant newVariant = productVariantMapper.toEntity(vReq);
                newVariant.setProduct(product);
                product.getVariants().add(newVariant);
            }
        }
        product.setCategory(category);
        product.setBrand(brand);
        productRepository.save(product);
        return productMapper.toDto(product);
    }

    @Override
    @Transactional
    public ProductResponse updateStatusProduct(Long id, Boolean isActive) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm có id: " + id));
        product.setIsActive(isActive);
        productRepository.save(product);
        return productMapper.toDto(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductImportResponse importProductsFromS3(String fileKey) {
        List<ProductExcelRowDto> rawRows;
        try (InputStream s3InputStream = fileStorageService.getFileInputStream(fileKey)) {
            rawRows = ExcelHelper.readProductExcel(s3InputStream);
        } catch (Exception e) {
            throw new RuntimeException("Không thể đọc file từ AWS S3: " + e.getMessage(), e);
        }

        if (rawRows == null || rawRows.isEmpty()) {
            throw new IllegalArgumentException("File Excel không có dữ liệu!");
        }

        List<String> allBarcodes = rawRows.stream()
                .map(ProductExcelRowDto::getBarcode)
                .filter(b -> b != null && !b.isBlank())
                .toList();

        List<String> allSkus = rawRows.stream()
                .map(ProductExcelRowDto::getSku)
                .filter(s -> s != null && !s.isBlank())
                .toList();

        final int CHECK_BATCH_SIZE = 1000;
        Set<String> existingDbBarcodes = new HashSet<>();
        for (int i = 0; i < allBarcodes.size(); i += CHECK_BATCH_SIZE) {
            List<String> batch = allBarcodes.subList(i, Math.min(i + CHECK_BATCH_SIZE, allBarcodes.size()));
            existingDbBarcodes.addAll(productVariantRepository.findExistingBarcodes(batch));
        }

        Set<String> existingDbSkus = new HashSet<>();
        for (int i = 0; i < allSkus.size(); i += CHECK_BATCH_SIZE) {
            List<String> batch = allSkus.subList(i, Math.min(i + CHECK_BATCH_SIZE, allSkus.size()));
            existingDbSkus.addAll(productVariantRepository.findExistingSkus(batch));
        }

        Map<Long, Category> categoryMap = categoryRepository.findAll().stream()
                .collect(Collectors.toMap(Category::getId, c -> c));
        Map<Long, Brand> brandMap = brandRepository.findAll().stream()
                .collect(Collectors.toMap(Brand::getId, b -> b));

        List<String> errors = new ArrayList<>();
        Set<String> scannedBarcodesInFile = new HashSet<>();
        Set<String> scannedSkusInFile = new HashSet<>();
        List<ProductExcelRowDto> validRows = new ArrayList<>();

        for (ProductExcelRowDto row : rawRows) {
            int line = row.getRowIndex();
            if (row.getProductCode() == null || row.getProductCode().isBlank()) {
                errors.add("Dòng " + line + ": Mã sản phẩm không được để trống.");
                continue;
            }
            if (row.getBarcode() == null || row.getBarcode().isBlank()) {
                errors.add("Dòng " + line + ": Mã barcode không được để trống.");
                continue;
            }
            if (row.getSku() == null || row.getSku().isBlank()) {
                errors.add("Dòng " + line + ": Mã SKU không được để trống.");
                continue;
            }

            // Kiểm tra trùng Barcode trong file Excel
            if (!scannedBarcodesInFile.add(row.getBarcode())) {
                errors.add("Dòng " + line + ": Trùng mã Barcode [" + row.getBarcode() + "] trong file Excel.");
                continue;
            }
            // Kiểm tra trùng Barcode với Database
            if (existingDbBarcodes.contains(row.getBarcode())) {
                errors.add("Dòng " + line + ": Mã Barcode [" + row.getBarcode() + "] đã tồn tại trên hệ thống.");
                continue;
            }

            // Kiểm tra trùng SKU trong file Excel
            if (!scannedSkusInFile.add(row.getSku())) {
                errors.add("Dòng " + line + ": Trùng mã SKU [" + row.getSku() + "] trong file Excel.");
                continue;
            }
            // Kiểm tra trùng SKU với Database
            if (existingDbSkus.contains(row.getSku())) {
                errors.add("Dòng " + line + ": Mã SKU [" + row.getSku() + "] đã tồn tại trên hệ thống.");
                continue;
            }

            validRows.add(row);
        }

        Map<String, List<ProductExcelRowDto>> grouped = validRows.stream()
                .collect(Collectors.groupingBy(ProductExcelRowDto::getProductCode));

        List<Product> productsToSave = new ArrayList<>();
        int totalVariants = 0;

        for (Map.Entry<String, List<ProductExcelRowDto>> entry : grouped.entrySet()) {
            String code = entry.getKey();
            List<ProductExcelRowDto> variantRows = entry.getValue();
            ProductExcelRowDto first = variantRows.get(0);

            // Lấy Category từ Map trong RAM
            Category category = first.getCategoryId() != null ? categoryMap.get(first.getCategoryId()) : null;
            if (category == null) {
                errors.add("Sản phẩm [" + code + "]: Không tìm thấy Category ID " + first.getCategoryId());
                continue;
            }

            // Lấy Brand từ Map trong RAM
            Brand brand = first.getBrandId() != null ? brandMap.get(first.getBrandId()) : null;

            Product product = Product.builder()
                    .code(code)
                    .name(first.getProductName() != null ? first.getProductName() : code)
                    .season(first.getSeason())
                    .description(first.getDescription())
                    .isActive(true)
                    .category(category)
                    .brand(brand)
                    .variants(new ArrayList<>())
                    .build();

            for (ProductExcelRowDto vRow : variantRows) {
                ProductVariant variant = ProductVariant.builder()
                        .product(product)
                        .sku(vRow.getSku())
                        .barcode(vRow.getBarcode())
                        .size(vRow.getSize() != null ? vRow.getSize() : "Freesize")
                        .color(vRow.getColor() != null ? vRow.getColor() : "Mặc định")
                        .costPrice(vRow.getCostPrice())
                        .retailPrice(vRow.getRetailPrice())
                        .imageUrl(vRow.getImageUrl())
                        .build();

                product.getVariants().add(variant);
                totalVariants++;
            }

            productsToSave.add(product);
        }

        final int SAVE_BATCH_SIZE = 50;
        int totalProductsSaved = 0;

        for (int i = 0; i < productsToSave.size(); i += SAVE_BATCH_SIZE) {
            List<Product> batchChunk = productsToSave.subList(i, Math.min(i + SAVE_BATCH_SIZE, productsToSave.size()));
            productRepository.saveAll(batchChunk);
            productRepository.flush(); // Ép gửi các câu lệnh SQL xuống DB
            totalProductsSaved += batchChunk.size();
        }

        return ProductImportResponse.builder()
                .totalRows(rawRows.size())
                .totalProducts(totalProductsSaved)
                .totalVariants(totalVariants)
                .errors(errors)
                .build();
    }
}
