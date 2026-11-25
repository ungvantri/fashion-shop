package com.fashionshop.fashion_shop.service;

import com.fashionshop.fashion_shop.dto.request.CloseShiftRequest;
import com.fashionshop.fashion_shop.dto.request.OpenShiftRequest;
import com.fashionshop.fashion_shop.dto.response.PageResponse;
import com.fashionshop.fashion_shop.dto.response.WorkShiftResponse;
import com.fashionshop.fashion_shop.enums.ShiftStatus;

import java.time.LocalDate;

public interface WorkShiftService {

    WorkShiftResponse openShift(OpenShiftRequest request);

    WorkShiftResponse closeShift(CloseShiftRequest request);

    WorkShiftResponse getCurrentShift(Long userId);

    PageResponse<WorkShiftResponse> getWorkShifts(
            Long storeId,
            Long userId,
            ShiftStatus status,
            LocalDate fromDate,
            LocalDate toDate,
            Integer page,
            Integer size
    );
}
