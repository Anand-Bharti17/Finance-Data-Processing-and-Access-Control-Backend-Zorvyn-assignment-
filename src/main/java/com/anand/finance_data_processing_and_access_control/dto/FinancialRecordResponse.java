package com.anand.finance_data_processing_and_access_control.dto;

import com.anand.finance_data_processing_and_access_control.model.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record FinancialRecordResponse(
        Long id,
        BigDecimal amount,
        TransactionType type,
        String category,
        LocalDate date,
        String notes,
        LocalDateTime createdAt
) {}