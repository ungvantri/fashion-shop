package com.fashionshop.fashion_shop.service.impl;

import com.fashionshop.fashion_shop.dto.response.BestSellerProductResponse;
import com.fashionshop.fashion_shop.dto.response.DashboardReportResponse;
import com.fashionshop.fashion_shop.dto.response.RevenueReportItemResponse;
import com.fashionshop.fashion_shop.entity.Order;
import com.fashionshop.fashion_shop.entity.OrderItem;
import com.fashionshop.fashion_shop.entity.Product;
import com.fashionshop.fashion_shop.enums.OrderStatus;
import com.fashionshop.fashion_shop.repository.CustomerRepository;
import com.fashionshop.fashion_shop.repository.OrderRepository;
import com.fashionshop.fashion_shop.repository.StoreInventoryRepository;
import com.fashionshop.fashion_shop.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private StoreInventoryRepository storeInventoryRepository;

    @Override
    public DashboardReportResponse getDashboardReport() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        Page<Order> todayOrders = orderRepository.findOrders(null, null, OrderStatus.COMPLETED, startOfDay, endOfDay, PageRequest.of(0, 1000));
        BigDecimal todayRevenue = todayOrders.getContent().stream()
                .map(Order::getFinalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long newCustomerCount = customerRepository.count();
        long lowStockCount = storeInventoryRepository.findLowStockInventories(null).size();

        return DashboardReportResponse.builder()
                .todayRevenue(todayRevenue)
                .todayOrderCount(todayOrders.getTotalElements())
                .newCustomerCount(newCustomerCount)
                .lowStockItemCount(lowStockCount)
                .build();
    }

    @Override
    public List<RevenueReportItemResponse> getRevenueReport(String groupBy, LocalDate fromDate, LocalDate toDate) {
        LocalDateTime start = (fromDate != null) ? fromDate.atStartOfDay() : LocalDate.now().minusDays(30).atStartOfDay();
        LocalDateTime end = (toDate != null) ? toDate.atTime(LocalTime.MAX) : LocalDate.now().atTime(LocalTime.MAX);

        Page<Order> ordersPage = orderRepository.findOrders(null, null, OrderStatus.COMPLETED, start, end, PageRequest.of(0, 5000));
        List<Order> orders = ordersPage.getContent();

        Map<String, List<Order>> grouped;
        if ("STORE".equalsIgnoreCase(groupBy)) {
            grouped = orders.stream().collect(Collectors.groupingBy(o -> o.getStore().getName()));
        } else if ("STAFF".equalsIgnoreCase(groupBy)) {
            grouped = orders.stream().collect(Collectors.groupingBy(o -> o.getCashier().getFullName()));
        } else if ("MONTH".equalsIgnoreCase(groupBy)) {
            grouped = orders.stream().collect(Collectors.groupingBy(o -> o.getCreatedAt().getYear() + "-" + String.format("%02d", o.getCreatedAt().getMonthValue())));
        } else {
            // Default: DAY
            grouped = orders.stream().collect(Collectors.groupingBy(o -> o.getCreatedAt().toLocalDate().toString()));
        }

        List<RevenueReportItemResponse> results = new ArrayList<>();
        grouped.forEach((key, orderList) -> {
            BigDecimal rev = orderList.stream().map(Order::getFinalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            results.add(RevenueReportItemResponse.builder()
                    .groupKey(key)
                    .orderCount((long) orderList.size())
                    .revenue(rev)
                    .build());
        });

        results.sort(Comparator.comparing(RevenueReportItemResponse::getGroupKey));
        return results;
    }

    @Override
    public List<BestSellerProductResponse> getBestSellers(String type, Integer limit) {
        int maxResults = (limit != null && limit > 0) ? limit : 10;
        Page<Order> ordersPage = orderRepository.findOrders(null, null, OrderStatus.COMPLETED, null, null, PageRequest.of(0, 1000));

        Map<Long, ProductSalesAgg> aggMap = new HashMap<>();

        for (Order order : ordersPage.getContent()) {
            if (order.getItems() == null) continue;
            for (OrderItem item : order.getItems()) {
                if (item.getProductVariant() == null || item.getProductVariant().getProduct() == null) continue;
                Product product = item.getProductVariant().getProduct();

                ProductSalesAgg agg = aggMap.computeIfAbsent(product.getId(), k -> new ProductSalesAgg(
                        product.getId(),
                        product.getCode(),
                        product.getName(),
                        0L,
                        BigDecimal.ZERO
                ));
                agg.totalSold += item.getQuantity();
                agg.totalRevenue = agg.totalRevenue.add(item.getTotalAmount());
            }
        }

        List<BestSellerProductResponse> list = aggMap.values().stream()
                .map(a -> BestSellerProductResponse.builder()
                        .productId(a.productId)
                        .productCode(a.productCode)
                        .productName(a.productName)
                        .totalSoldQuantity(a.totalSold)
                        .totalRevenue(a.totalRevenue)
                        .build())
                .collect(Collectors.toList());

        if ("SLOW_MOVING".equalsIgnoreCase(type)) {
            list.sort(Comparator.comparing(BestSellerProductResponse::getTotalSoldQuantity));
        } else {
            list.sort(Comparator.comparing(BestSellerProductResponse::getTotalSoldQuantity).reversed());
        }

        return list.stream().limit(maxResults).toList();
    }

    private static class ProductSalesAgg {
        Long productId;
        String productCode;
        String productName;
        Long totalSold;
        BigDecimal totalRevenue;

        ProductSalesAgg(Long productId, String productCode, String productName, Long totalSold, BigDecimal totalRevenue) {
            this.productId = productId;
            this.productCode = productCode;
            this.productName = productName;
            this.totalSold = totalSold;
            this.totalRevenue = totalRevenue;
        }
    }
}
