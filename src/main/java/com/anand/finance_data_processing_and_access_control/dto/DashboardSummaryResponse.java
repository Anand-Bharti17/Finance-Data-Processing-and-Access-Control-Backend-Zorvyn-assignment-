package com.anand.finance_data_processing_and_access_control.dto;

import java.math.BigDecimal;
import java.util.Map;

public record DashboardSummaryResponse(
        BigDecimal totalIncome,
        BigDecimal totalExpenses,
        BigDecimal netBalance,
        Map<String, BigDecimal> categoryTotals
) {}