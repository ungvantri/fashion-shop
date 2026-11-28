package com.fashionshop.fashion_shop.service.impl;

import com.fashionshop.fashion_shop.dto.request.*;
import com.fashionshop.fashion_shop.dto.response.OrderResponse;
import com.fashionshop.fashion_shop.dto.response.PageResponse;
import com.fashionshop.fashion_shop.dto.response.ReturnOrderResponse;
import com.fashionshop.fashion_shop.dto.response.VoucherValidationResponse;
import com.fashionshop.fashion_shop.entity.*;
import com.fashionshop.fashion_shop.enums.OrderChannel;
import com.fashionshop.fashion_shop.enums.OrderStatus;
import com.fashionshop.fashion_shop.enums.PaymentMethod;
import com.fashionshop.fashion_shop.exception.ConflictException;
import com.fashionshop.fashion_shop.exception.ResourceNotFoundException;
import com.fashionshop.fashion_shop.mapper.OrderMapper;
import com.fashionshop.fashion_shop.mapper.ReturnOrderMapper;
import com.fashionshop.fashion_shop.repository.*;
import com.fashionshop.fashion_shop.service.OrderService;
import com.fashionshop.fashion_shop.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerTierRepository customerTierRepository;

    @Autowired
    private WorkShiftRepository workShiftRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private StoreInventoryRepository storeInventoryRepository;

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private ReturnOrderRepository returnOrderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ReturnOrderMapper returnOrderMapper;

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "inventories", allEntries = true),
            @CacheEvict(value = "reports", allEntries = true),
            @CacheEvict(value = "customers", allEntries = true),
            @CacheEvict(value = "vouchers", allEntries = true)
    })
    public OrderResponse createOrder(OrderCreateRequest request) {
        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chi nhánh với ID: " + request.getStoreId()));

        User cashier = userRepository.findById(request.getCashierId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thu ngân với ID: " + request.getCashierId()));

        User seller = null;
        if (request.getSellerId() != null) {
            seller = userRepository.findById(request.getSellerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên bán hàng với ID: " + request.getSellerId()));
        }

        Customer customer = null;
        if (request.getCustomerId() != null) {
            customer = customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng với ID: " + request.getCustomerId()));
        }

        WorkShift shift = null;
        if (request.getShiftId() != null) {
            shift = workShiftRepository.findById(request.getShiftId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ca làm việc với ID: " + request.getShiftId()));
        }

        BigDecimal totalItemAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequest itemReq : request.getItems()) {
            ProductVariant variant = productVariantRepository.findById(itemReq.getProductVariantId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy biến thể sản phẩm với ID: " + itemReq.getProductVariantId()));

            StoreInventory inventory = storeInventoryRepository.findByStoreIdAndProductVariantId(store.getId(), variant.getId())
                    .orElseThrow(() -> new ConflictException("Sản phẩm " + variant.getSku() + " chưa có tồn kho tại chi nhánh " + store.getName()));

            if (inventory.getQuantity() < itemReq.getQuantity()) {
                throw new ConflictException("Tồn kho không đủ. Sản phẩm " + variant.getSku()
                        + " chỉ còn " + inventory.getQuantity() + " nhưng yêu cầu " + itemReq.getQuantity());
            }

            inventory.setQuantity(inventory.getQuantity() - itemReq.getQuantity());

            BigDecimal actualRetailPrice = variant.getRetailPrice() != null ? variant.getRetailPrice() : BigDecimal.ZERO;
            BigDecimal lineAmount = actualRetailPrice.multiply(BigDecimal.valueOf(itemReq.getQuantity()))
                    .subtract(itemReq.getDiscountAmount() != null ? itemReq.getDiscountAmount() : BigDecimal.ZERO);
            if (lineAmount.compareTo(BigDecimal.ZERO) < 0) {
                lineAmount = BigDecimal.ZERO;
            }
            totalItemAmount = totalItemAmount.add(lineAmount);

            OrderItem orderItem = OrderItem.builder()
                    .productVariant(variant)
                    .quantity(itemReq.getQuantity())
                    .retailPrice(actualRetailPrice)
                    .costPrice(variant.getCostPrice() != null ? variant.getCostPrice() : BigDecimal.ZERO)
                    .discountAmount(itemReq.getDiscountAmount())
                    .totalAmount(lineAmount)
                    .build();
            orderItems.add(orderItem);
        }

        BigDecimal voucherDiscount = BigDecimal.ZERO;
        if (request.getVoucherCode() != null && !request.getVoucherCode().isBlank()) {
            String voucherCode = request.getVoucherCode().trim().toUpperCase();
            VoucherValidationResponse validation = voucherService.validateVoucher(
                    VoucherValidationRequest.builder()
                            .code(voucherCode)
                            .orderAmount(totalItemAmount)
                            .build()
            );

            if (!Boolean.TRUE.equals(validation.getIsValid())) {
                throw new ConflictException("Voucher không hợp lệ: " + validation.getMessage());
            }

            voucherDiscount = validation.getCalculatedDiscountAmount() != null ? validation.getCalculatedDiscountAmount() : BigDecimal.ZERO;

            Voucher voucher = voucherRepository.findByCode(voucherCode)
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy mã voucher: " + voucherCode));
            voucher.setUsedCount(voucher.getUsedCount() + 1);
            voucherRepository.save(voucher);
        }

        BigDecimal finalAmount = totalItemAmount.subtract(voucherDiscount);
        if (finalAmount.compareTo(BigDecimal.ZERO) < 0) {
            finalAmount = BigDecimal.ZERO;
        }

        // 3. Kiểm tra tổng số tiền thanh toán có khớp với finalAmount không
        BigDecimal totalPaid = BigDecimal.ZERO;
        BigDecimal cashPaymentAmount = BigDecimal.ZERO;
        List<OrderPayment> payments = new ArrayList<>();

        for (OrderPaymentRequest payReq : request.getPayments()) {
            totalPaid = totalPaid.add(payReq.getAmount());
            if (payReq.getPaymentMethod() == PaymentMethod.CASH) {
                cashPaymentAmount = cashPaymentAmount.add(payReq.getAmount());
            }

            OrderPayment payment = OrderPayment.builder()
                    .paymentMethod(payReq.getPaymentMethod())
                    .amount(payReq.getAmount())
                    .transactionReference(payReq.getTransactionReference())
                    .build();
            payments.add(payment);
        }

        if (totalPaid.compareTo(finalAmount) < 0) {
            throw new ConflictException("Số tiền thanh toán (" + totalPaid + ") chưa đủ tổng tiền đơn hàng (" + finalAmount + ")");
        }

        if (shift != null && cashPaymentAmount.compareTo(BigDecimal.ZERO) > 0) {
            shift.setSystemCash(shift.getSystemCash().add(cashPaymentAmount));
            workShiftRepository.save(shift);
        }

        if (customer != null) {
            int earnedPoints = finalAmount.divide(BigDecimal.valueOf(100000), 0, RoundingMode.DOWN).intValue();
            customer.setCurrentPoints(customer.getCurrentPoints() + earnedPoints);
            customer.setTotalSpent(customer.getTotalSpent().add(finalAmount));

            // Tự động nâng hạng thẻ nếu đủ điều kiện
            customerTierRepository.findTierBySpent(customer.getTotalSpent()).ifPresent(customer::setTier);
            customerRepository.save(customer);
        }

        // 6. Lưu đơn hàng
        String orderCode = generateOrderCode();
        Order order = Order.builder()
                .orderCode(orderCode)
                .store(store)
                .customer(customer)
                .shift(shift)
                .cashier(cashier)
                .seller(seller)
                .channel(request.getChannel())
                .totalItemAmount(totalItemAmount)
                .discountAmount(voucherDiscount)
                .finalAmount(finalAmount)
                .status(OrderStatus.COMPLETED)
                .createdAt(LocalDateTime.now())
                .items(orderItems)
                .payments(payments)
                .build();

        for (OrderItem oi : orderItems) {
            oi.setOrder(order);
        }
        for (OrderPayment op : payments) {
            op.setOrder(order);
        }

        Order saved = orderRepository.save(order);
        return orderMapper.toDto(saved);
    }

    @Override
    public PageResponse<OrderResponse> getOrders(
            Long storeId,
            OrderChannel channel,
            OrderStatus status,
            LocalDate fromDate,
            LocalDate toDate,
            Integer page,
            Integer size
    ) {
        int pageIndex = (page != null && page > 0) ? page - 1 : 0;
        int pageSize = (size != null && size > 0) ? size : 20;
        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by("id").descending());

        LocalDateTime start = (fromDate != null) ? fromDate.atStartOfDay() : null;
        LocalDateTime end = (toDate != null) ? toDate.atTime(LocalTime.MAX) : null;

        Page<Order> pageData = orderRepository.findOrders(storeId, channel, status, start, end, pageable);
        List<OrderResponse> items = orderMapper.toListDto(pageData.getContent());

        return PageResponse.<OrderResponse>builder()
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
    @Cacheable(value = "orders", key = "'code:' + #orderCode")
    public OrderResponse getOrderByCode(String orderCode) {
        Order order = orderRepository.findByOrderCodeWithDetails(orderCode)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng với mã: " + orderCode));
        return orderMapper.toDto(order);
    }

    @Override
    public void sendReceipt(String orderCode, String recipientEmail) {
        Order order = orderRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng với mã: " + orderCode));
        // Giả lập gửi hóa đơn điện tử K80 qua email
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "orders", allEntries = true),
            @CacheEvict(value = "reports", allEntries = true),
            @CacheEvict(value = "inventories", allEntries = true)
    })
    public ReturnOrderResponse createReturn(ReturnOrderRequest request) {
        Order originalOrder = orderRepository.findById(request.getOriginalOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng gốc với ID: " + request.getOriginalOrderId()));

        // Kiểm tra chính sách đổi trả trong vòng 7 ngày
        long daysBetween = ChronoUnit.DAYS.between(originalOrder.getCreatedAt(), LocalDateTime.now());
        if (daysBetween > 7) {
            throw new ConflictException("Đơn hàng đã quá thời hạn đổi trả 7 ngày (Mua ngày " + originalOrder.getCreatedAt().toLocalDate() + ")");
        }

        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chi nhánh với ID: " + request.getStoreId()));

        User createdBy = userRepository.findById(request.getCreatedById())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên tiếp nhận với ID: " + request.getCreatedById()));

        // 1. Lấy thông tin các mặt hàng trong đơn hàng gốc
        Map<Long, OrderItem> originalItemMap = new HashMap<>();
        if (originalOrder.getItems() != null) {
            for (OrderItem oi : originalOrder.getItems()) {
                originalItemMap.put(oi.getProductVariant().getId(), oi);
            }
        }

        // 2. Lấy số lượng đã từng hoàn trả trong các lần trước (nếu có)
        List<ReturnOrder> pastReturns = returnOrderRepository.findByOriginalOrderId(originalOrder.getId());
        Map<Long, Integer> alreadyReturnedQuantities = new HashMap<>();
        for (ReturnOrder pastReturn : pastReturns) {
            if (pastReturn.getItems() != null) {
                for (ReturnItem ri : pastReturn.getItems()) {
                    Long variantId = ri.getProductVariant().getId();
                    alreadyReturnedQuantities.put(variantId,
                            alreadyReturnedQuantities.getOrDefault(variantId, 0) + ri.getQuantity());
                }
            }
        }

        // 3. Kiểm tra tính hợp lệ của từng sản phẩm yêu cầu trả và tự tính toán số tiền hoàn tối đa
        BigDecimal calculatedRefundAmount = BigDecimal.ZERO;
        String returnCode = generateReturnCode();
        ReturnOrder returnOrder = ReturnOrder.builder()
                .returnCode(returnCode)
                .originalOrder(originalOrder)
                .store(store)
                .createdBy(createdBy)
                .status("COMPLETED")
                .createdAt(LocalDateTime.now())
                .items(new ArrayList<>())
                .build();

        for (ReturnItemRequest itemReq : request.getItems()) {
            Long variantId = itemReq.getProductVariantId();
            if (!originalItemMap.containsKey(variantId)) {
                throw new ConflictException("Sản phẩm với ID " + variantId + " không có trong đơn hàng gốc #" + originalOrder.getOrderCode());
            }

            OrderItem originalItem = originalItemMap.get(variantId);
            int previouslyReturned = alreadyReturnedQuantities.getOrDefault(variantId, 0);
            int availableToReturn = originalItem.getQuantity() - previouslyReturned;

            if (itemReq.getQuantity() > availableToReturn) {
                throw new ConflictException("Số lượng trả (" + itemReq.getQuantity() + ") vượt quá số lượng còn lại có thể trả ("
                        + availableToReturn + ") của sản phẩm " + originalItem.getProductVariant().getSku());
            }

            ProductVariant variant = originalItem.getProductVariant();

            // Tính đơn giá thực trả của mặt hàng đó sau chiết khấu dòng hàng
            BigDecimal unitActualPrice = originalItem.getTotalAmount()
                    .divide(BigDecimal.valueOf(originalItem.getQuantity()), 2, RoundingMode.HALF_UP);
            BigDecimal itemRefund = unitActualPrice.multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            calculatedRefundAmount = calculatedRefundAmount.add(itemRefund);

            // Hoàn trả số lượng vào tồn kho chi nhánh tiếp nhận
            StoreInventory inventory = storeInventoryRepository.findByStoreIdAndProductVariantId(store.getId(), variant.getId())
                    .orElseGet(() -> StoreInventory.builder()
                            .store(store)
                            .productVariant(variant)
                            .quantity(0)
                            .minThreshold(5)
                            .build());

            inventory.setQuantity(inventory.getQuantity() + itemReq.getQuantity());
            storeInventoryRepository.save(inventory);

            ReturnItem returnItem = ReturnItem.builder()
                    .returnOrder(returnOrder)
                    .productVariant(variant)
                    .quantity(itemReq.getQuantity())
                    .returnReason(itemReq.getReturnReason())
                    .build();
            returnOrder.getItems().add(returnItem);
        }

        // Nếu client có gửi refundAmount nhưng vượt quá số tiền tối đa được hoàn dựa trên giá mua thực tế
        BigDecimal finalRefundAmount = calculatedRefundAmount;
        if (request.getRefundAmount() != null && request.getRefundAmount().compareTo(BigDecimal.ZERO) > 0) {
            if (request.getRefundAmount().compareTo(calculatedRefundAmount) > 0) {
                throw new ConflictException("Số tiền hoàn trả yêu cầu (" + request.getRefundAmount()
                        + ") vượt quá số tiền tối đa được hoàn (" + calculatedRefundAmount + ")");
            }
            finalRefundAmount = request.getRefundAmount();
        }
        returnOrder.setRefundAmount(finalRefundAmount);

        ReturnOrder saved = returnOrderRepository.save(returnOrder);
        return returnOrderMapper.toDto(saved);
    }

    @Override
    public PageResponse<ReturnOrderResponse> getReturns(Long storeId, Integer page, Integer size) {
        int pageIndex = (page != null && page > 0) ? page - 1 : 0;
        int pageSize = (size != null && size > 0) ? size : 20;
        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by("id").descending());

        Page<ReturnOrder> pageData = returnOrderRepository.findReturns(storeId, pageable);
        List<ReturnOrderResponse> items = returnOrderMapper.toListDto(pageData.getContent());

        return PageResponse.<ReturnOrderResponse>builder()
                .items(items)
                .currentPage(pageData.getNumber() + 1)
                .pageSize(pageData.getSize())
                .totalItems(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .hasNext(pageData.hasNext())
                .hasPrevious(pageData.hasPrevious())
                .build();
    }

    private String generateOrderCode() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));
        String randomSuffix = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return "ORD-" + timestamp + "-" + randomSuffix;
    }

    private String generateReturnCode() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));
        String randomSuffix = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return "RET-" + timestamp + "-" + randomSuffix;
    }
}
