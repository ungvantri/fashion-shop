package com.fashionshop.fashion_shop.service;

import com.fashionshop.fashion_shop.dto.response.BestSellerProductResponse;
import com.fashionshop.fashion_shop.dto.response.DashboardReportResponse;
import com.fashionshop.fashion_shop.dto.response.RevenueReportItemResponse;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {

    DashboardReportResponse getDashboardReport();

    List<RevenueReportItemResponse> getRevenueReport(String groupBy, LocalDate fromDate, LocalDate toDate);

    List<BestSellerProductResponse> getBestSellers(String type, Integer limit);
}
