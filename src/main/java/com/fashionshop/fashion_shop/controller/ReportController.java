package com.fashionshop.fashion_shop.controller;

import com.fashionshop.fashion_shop.dto.response.ApiResponse;
import com.fashionshop.fashion_shop.dto.response.BestSellerProductResponse;
import com.fashionshop.fashion_shop.dto.response.DashboardReportResponse;
import com.fashionshop.fashion_shop.dto.response.RevenueReportItemResponse;
import com.fashionshop.fashion_shop.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardReportResponse>> getDashboardReport() {
        DashboardReportResponse response = reportService.getDashboardReport();
        ApiResponse<DashboardReportResponse> result = ApiResponse.<DashboardReportResponse>builder()
                .success(true)
                .message("Lấy chỉ số kinh doanh tổng quan thành công")
                .data(response)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/revenue")
    public ResponseEntity<ApiResponse<List<RevenueReportItemResponse>>> getRevenueReport(
            @RequestParam(defaultValue = "DAY") String groupBy,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
    ) {
        List<RevenueReportItemResponse> response = reportService.getRevenueReport(groupBy, fromDate, toDate);
        ApiResponse<List<RevenueReportItemResponse>> result = ApiResponse.<List<RevenueReportItemResponse>>builder()
                .success(true)
                .message("Lấy báo cáo doanh thu thành công")
                .data(response)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/products/best-sellers")
    public ResponseEntity<ApiResponse<List<BestSellerProductResponse>>> getBestSellers(
            @RequestParam(defaultValue = "BEST_SELLER") String type,
            @RequestParam(defaultValue = "10") Integer limit
    ) {
        List<BestSellerProductResponse> response = reportService.getBestSellers(type, limit);
        ApiResponse<List<BestSellerProductResponse>> result = ApiResponse.<List<BestSellerProductResponse>>builder()
                .success(true)
                .message("Lấy báo cáo top sản phẩm thành công")
                .data(response)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
