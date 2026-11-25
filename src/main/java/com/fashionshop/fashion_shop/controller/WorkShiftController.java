package com.fashionshop.fashion_shop.controller;

import com.fashionshop.fashion_shop.dto.request.CloseShiftRequest;
import com.fashionshop.fashion_shop.dto.request.OpenShiftRequest;
import com.fashionshop.fashion_shop.dto.response.ApiResponse;
import com.fashionshop.fashion_shop.dto.response.PageResponse;
import com.fashionshop.fashion_shop.dto.response.WorkShiftResponse;
import com.fashionshop.fashion_shop.enums.ShiftStatus;
import com.fashionshop.fashion_shop.service.WorkShiftService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/shifts")
public class WorkShiftController {

    @Autowired
    private WorkShiftService workShiftService;

    @PostMapping("/open")
    public ResponseEntity<ApiResponse<WorkShiftResponse>> openShift(@Valid @RequestBody OpenShiftRequest request) {
        WorkShiftResponse response = workShiftService.openShift(request);
        ApiResponse<WorkShiftResponse> result = ApiResponse.<WorkShiftResponse>builder()
                .success(true)
                .message("Mở ca làm việc thành công")
                .data(response)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/close")
    public ResponseEntity<ApiResponse<WorkShiftResponse>> closeShift(@Valid @RequestBody CloseShiftRequest request) {
        WorkShiftResponse response = workShiftService.closeShift(request);
        ApiResponse<WorkShiftResponse> result = ApiResponse.<WorkShiftResponse>builder()
                .success(true)
                .message("Đóng ca làm việc và đối soát tiền mặt thành công")
                .data(response)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/current")
    public ResponseEntity<ApiResponse<WorkShiftResponse>> getCurrentShift(@RequestParam Long userId) {
        WorkShiftResponse response = workShiftService.getCurrentShift(userId);
        ApiResponse<WorkShiftResponse> result = ApiResponse.<WorkShiftResponse>builder()
                .success(true)
                .message("Lấy thông tin ca làm việc hiện tại thành công")
                .data(response)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<WorkShiftResponse>>> getWorkShifts(
            @RequestParam(required = false) Long storeId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) ShiftStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size
    ) {
        PageResponse<WorkShiftResponse> pageResponse = workShiftService.getWorkShifts(
                storeId, userId, status, fromDate, toDate, page, size
        );
        ApiResponse<PageResponse<WorkShiftResponse>> result = ApiResponse.<PageResponse<WorkShiftResponse>>builder()
                .success(true)
                .message("Lấy danh sách ca làm việc thành công")
                .data(pageResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
