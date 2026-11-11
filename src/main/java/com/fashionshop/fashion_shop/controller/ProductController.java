package com.fashionshop.fashion_shop.controller;

import com.fashionshop.fashion_shop.dto.request.ProductRequest;
import com.fashionshop.fashion_shop.dto.response.ApiResponse;
import com.fashionshop.fashion_shop.dto.response.PageResponse;
import com.fashionshop.fashion_shop.dto.response.ProductImportResponse;
import com.fashionshop.fashion_shop.dto.response.ProductResponse;
import com.fashionshop.fashion_shop.service.FileStorageService;
import com.fashionshop.fashion_shop.service.ProductSearchService;
import com.fashionshop.fashion_shop.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ProductSearchService productSearchService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String season,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size
    ) {
        PageResponse<ProductResponse> pageResponse = productService.getProducts(search, categoryId, season, isActive, page, size);
        ApiResponse<PageResponse<ProductResponse>> result = ApiResponse.<PageResponse<ProductResponse>>builder()
                .success(true)
                .message("Lấy danh sách sản phẩm theo trang thành công")
                .data(pageResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize
    ) {
        int pageIndex = (page > 0) ? page - 1 : 0;
        PageResponse<ProductResponse> response = productSearchService.searchProducts(
                keyword, categoryId, brandId, color, size, minPrice, maxPrice, pageIndex, pageSize
        );
        ApiResponse<PageResponse<ProductResponse>> result = ApiResponse.<PageResponse<ProductResponse>>builder()
                .success(true)
                .message("Tìm kiếm sản phẩm thành công từ Elasticsearch")
                .data(response)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        ProductResponse productResponse = productService.createProduct(productRequest);
        ApiResponse<ProductResponse> result = ApiResponse.<ProductResponse>builder()
                .success(true)
                .message("Tạo sản phẩm cha kèm danh sách biến thể thành công")
                .data(productResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Long id) {
        ProductResponse productResponse = productService.getProductById(id);
        ApiResponse<ProductResponse> result = ApiResponse.<ProductResponse>builder()
                .success(true)
                .message("Lấy thông tin chi tiết sản phẩm thành công")
                .data(productResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest productRequest) {
        ProductResponse productResponse = productService.updateProduct(id, productRequest);
        ApiResponse<ProductResponse> result = ApiResponse.<ProductResponse>builder()
                .success(true)
                .message("Cập nhật thông tin sản phẩm và các biến thể thành công")
                .data(productResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<ProductResponse>> updateStatusProduct(@PathVariable Long id, @RequestParam Boolean isActive) {
        ProductResponse productResponse = productService.updateStatusProduct(id, isActive);
        ApiResponse<ProductResponse> result = ApiResponse.<ProductResponse>builder()
                .success(true)
                .message("Cập nhật trạng thái sản phẩm thành công")
                .data(productResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    @GetMapping("/import-excel/presigned-url")
    public ResponseEntity<ApiResponse<String>> getPresignedUploadUrl(@RequestParam String fileName) {
        String uploadUrl = fileStorageService.generateUploadUrl(fileName);
        ApiResponse<String> result = ApiResponse.<String>builder()
                .success(true)
                .message("Tạo đường dẫn upload S3 thành công")
                .data(uploadUrl)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/import-excel/process")
    public ResponseEntity<ApiResponse<ProductImportResponse>> processExcelFromS3(@RequestBody Map<String, String> request) {
        String fileKey = request.get("fileKey");
        ProductImportResponse response = productService.importProductsFromS3(fileKey);
        ApiResponse<ProductImportResponse> result = ApiResponse.<ProductImportResponse>builder()
                .success(true)
                .message("Nhập dữ liệu sản phẩm từ S3 vào database thành công")
                .data(response)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
