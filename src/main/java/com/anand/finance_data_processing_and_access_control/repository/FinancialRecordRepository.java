package com.anand.finance_data_processing_and_access_control.repository;

import com.anand.finance_data_processing_and_access_control.model.entity.FinancialRecord;
import com.anand.finance_data_processing_and_access_control.model.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {

    // Pagination for a specific user
    Page<FinancialRecord> findByUserId(Long userId, Pageable pageable);

    // Ensure users can only delete/find their OWN records
    Optional<FinancialRecord> findByIdAndUserId(Long id, Long userId);

    // Search functionality with Pagination
    @Query("SELECT f FROM FinancialRecord f WHERE f.user.id = :userId AND " +
            "(:keyword IS NULL OR LOWER(f.category) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(f.notes) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<FinancialRecord> searchRecords(@Param("userId") Long userId, @Param("keyword") String keyword, Pageable pageable);

    // Dashboard Summary Queries
    @Query("SELECT SUM(f.amount) FROM FinancialRecord f WHERE f.user.id = :userId AND f.type = :type")
    BigDecimal sumAmountByUserIdAndType(@Param("userId") Long userId, @Param("type") TransactionType type);

    @Query("SELECT f.category, SUM(f.amount) FROM FinancialRecord f WHERE f.user.id = :userId AND f.type = :type GROUP BY f.category")
    List<Object[]> sumAmountByUserIdAndTypeGroupByCategory(@Param("userId") Long userId, @Param("type") TransactionType type);
}