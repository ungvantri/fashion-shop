package com.fashionshop.fashion_shop.repository;

import com.fashionshop.fashion_shop.entity.WorkShift;
import com.fashionshop.fashion_shop.enums.ShiftStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface WorkShiftRepository extends JpaRepository<WorkShift, Long> {

    // Tìm ca làm việc đang mở của 1 nhân viên
    Optional<WorkShift> findByUserIdAndStatus(Long userId, ShiftStatus status);

    // Kiểm tra nhân viên có ca nào đang OPEN không
    boolean existsByUserIdAndStatus(Long userId, ShiftStatus status);

    @Query(value = "SELECT ws FROM WorkShift ws " +
            "JOIN FETCH ws.store s " +
            "JOIN FETCH ws.user u " +
            "WHERE (:storeId IS NULL OR s.id = :storeId) " +
            "AND (:userId IS NULL OR u.id = :userId) " +
            "AND (:status IS NULL OR ws.status = :status) " +
            "AND (:fromDate IS NULL OR ws.openedAt >= :fromDate) " +
            "AND (:toDate IS NULL OR ws.openedAt <= :toDate)",
            countQuery = "SELECT COUNT(ws) FROM WorkShift ws " +
            "WHERE (:storeId IS NULL OR ws.store.id = :storeId) " +
            "AND (:userId IS NULL OR ws.user.id = :userId) " +
            "AND (:status IS NULL OR ws.status = :status) " +
            "AND (:fromDate IS NULL OR ws.openedAt >= :fromDate) " +
            "AND (:toDate IS NULL OR ws.openedAt <= :toDate)")
    Page<WorkShift> findWorkShifts(
            @Param("storeId") Long storeId,
            @Param("userId") Long userId,
            @Param("status") ShiftStatus status,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            Pageable pageable
    );
}
