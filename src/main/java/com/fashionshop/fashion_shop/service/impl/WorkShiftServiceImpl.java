package com.fashionshop.fashion_shop.service.impl;

import com.fashionshop.fashion_shop.dto.request.CloseShiftRequest;
import com.fashionshop.fashion_shop.dto.request.OpenShiftRequest;
import com.fashionshop.fashion_shop.dto.response.PageResponse;
import com.fashionshop.fashion_shop.dto.response.WorkShiftResponse;
import com.fashionshop.fashion_shop.entity.Store;
import com.fashionshop.fashion_shop.entity.User;
import com.fashionshop.fashion_shop.entity.WorkShift;
import com.fashionshop.fashion_shop.enums.ShiftStatus;
import com.fashionshop.fashion_shop.exception.ConflictException;
import com.fashionshop.fashion_shop.exception.ResourceNotFoundException;
import com.fashionshop.fashion_shop.mapper.WorkShiftMapper;
import com.fashionshop.fashion_shop.repository.StoreRepository;
import com.fashionshop.fashion_shop.repository.UserRepository;
import com.fashionshop.fashion_shop.repository.WorkShiftRepository;
import com.fashionshop.fashion_shop.service.WorkShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class WorkShiftServiceImpl implements WorkShiftService {

    @Autowired
    private WorkShiftRepository workShiftRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkShiftMapper workShiftMapper;

    @Override
    @Transactional
    public WorkShiftResponse openShift(OpenShiftRequest request) {
        if (workShiftRepository.existsByUserIdAndStatus(request.getUserId(), ShiftStatus.OPEN)) {
            throw new ConflictException("Nhân viên hiện đang có một ca làm việc chưa đóng");
        }

        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chi nhánh với ID: " + request.getStoreId()));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên với ID: " + request.getUserId()));

        WorkShift shift = WorkShift.builder()
                .store(store)
                .user(user)
                .openedAt(LocalDateTime.now())
                .initialCash(request.getInitialCash())
                .systemCash(request.getInitialCash())
                .status(ShiftStatus.OPEN)
                .build();

        WorkShift saved = workShiftRepository.save(shift);
        return workShiftMapper.toDto(saved);
    }

    @Override
    @Transactional
    public WorkShiftResponse closeShift(CloseShiftRequest request) {
        WorkShift shift = workShiftRepository.findById(request.getShiftId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ca làm việc với ID: " + request.getShiftId()));

        if (shift.getStatus() == ShiftStatus.CLOSED) {
            throw new ConflictException("Ca làm việc này đã được đóng trước đó");
        }

        BigDecimal systemCash = shift.getSystemCash() != null ? shift.getSystemCash() : shift.getInitialCash();
        BigDecimal actualCash = request.getActualCash();
        BigDecimal difference = actualCash.subtract(systemCash);

        shift.setClosedAt(LocalDateTime.now());
        shift.setActualCash(actualCash);
        shift.setDifference(difference);
        shift.setStatus(ShiftStatus.CLOSED);

        WorkShift saved = workShiftRepository.save(shift);
        return workShiftMapper.toDto(saved);
    }

    @Override
    public WorkShiftResponse getCurrentShift(Long userId) {
        WorkShift shift = workShiftRepository.findByUserIdAndStatus(userId, ShiftStatus.OPEN)
                .orElseThrow(() -> new ResourceNotFoundException("Nhân viên không có ca làm việc nào đang mở"));
        return workShiftMapper.toDto(shift);
    }

    @Override
    public PageResponse<WorkShiftResponse> getWorkShifts(
            Long storeId,
            Long userId,
            ShiftStatus status,
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

        Page<WorkShift> pageData = workShiftRepository.findWorkShifts(storeId, userId, status, start, end, pageable);
        List<WorkShiftResponse> items = workShiftMapper.toListDto(pageData.getContent());

        return PageResponse.<WorkShiftResponse>builder()
                .items(items)
                .currentPage(pageData.getNumber() + 1)
                .pageSize(pageData.getSize())
                .totalItems(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .hasNext(pageData.hasNext())
                .hasPrevious(pageData.hasPrevious())
                .build();
    }
}
