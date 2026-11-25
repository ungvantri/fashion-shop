package com.fashionshop.fashion_shop.mapper;

import com.fashionshop.fashion_shop.dto.response.WorkShiftResponse;
import com.fashionshop.fashion_shop.entity.WorkShift;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkShiftMapper {

    @Mapping(target = "storeId", source = "store.id")
    @Mapping(target = "storeName", source = "store.name")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.fullName")
    WorkShiftResponse toDto(WorkShift workShift);

    List<WorkShiftResponse> toListDto(List<WorkShift> workShifts);
}
