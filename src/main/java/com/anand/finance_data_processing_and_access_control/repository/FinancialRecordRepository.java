package com.anand.finance_data_processing_and_access_control.repository;

import com.anand.finance_data_processing_and_access_control.model.entity.FinancialRecord;
import com.anand.finance_data_processing_and_access_control.model.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {

    // Basic retrieval for a specific user
    List<FinancialRecord> findByUserId(Long userId);

    // Filtering queries for a specific user
    List<FinancialRecord> findByUserIdAndType(Long userId, TransactionType type);
    List<FinancialRecord> findByUserIdAndCategoryIgnoreCase(Long userId, String category);
    List<FinancialRecord> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    // Dashboard Summary Queries: Aggregating totals at the database level for efficiency
    @Query("SELECT SUM(f.amount) FROM FinancialRecord f WHERE f.user.id = :userId AND f.type = :type")
    BigDecimal sumAmountByUserIdAndType(@Param("userId") Long userId, @Param("type") TransactionType type);

    // Category-wise totals for the dashboard
    @Query("SELECT f.category, SUM(f.amount) FROM FinancialRecord f WHERE f.user.id = :userId AND f.type = :type GROUP BY f.category")
    List<Object[]> sumAmountByUserIdAndTypeGroupByCategory(@Param("userId") Long userId, @Param("type") TransactionType type);
}