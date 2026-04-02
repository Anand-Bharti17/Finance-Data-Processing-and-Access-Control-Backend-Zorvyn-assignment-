package com.anand.finance_data_processing_and_access_control.service;

import com.anand.finance_data_processing_and_access_control.dto.DashboardSummaryResponse;
import com.anand.finance_data_processing_and_access_control.dto.FinancialRecordRequest;
import com.anand.finance_data_processing_and_access_control.dto.FinancialRecordResponse;
import com.anand.finance_data_processing_and_access_control.exception.ResourceNotFoundException;
import com.anand.finance_data_processing_and_access_control.model.entity.FinancialRecord;
import com.anand.finance_data_processing_and_access_control.model.entity.User;
import com.anand.finance_data_processing_and_access_control.model.enums.TransactionType;
import com.anand.finance_data_processing_and_access_control.repository.FinancialRecordRepository;
import com.anand.finance_data_processing_and_access_control.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FinancialRecordService {

    private final FinancialRecordRepository recordRepository;
    private final UserRepository userRepository;

    @Transactional
    public FinancialRecordResponse createRecord(Long userId, FinancialRecordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        FinancialRecord record = new FinancialRecord();
        record.setUser(user);
        record.setAmount(request.amount());
        record.setType(request.type());
        record.setCategory(request.category());
        record.setDate(request.date());
        record.setNotes(request.notes());

        FinancialRecord savedRecord = recordRepository.save(record);
        return mapToResponse(savedRecord);
    }

    // Updated for Pagination and Search
    public Page<FinancialRecordResponse> getRecords(Long userId, String keyword, Pageable pageable) {
        Page<FinancialRecord> records;

        if (keyword != null && !keyword.trim().isEmpty()) {
            records = recordRepository.searchRecords(userId, keyword, pageable);
        } else {
            records = recordRepository.findByUserId(userId, pageable);
        }

        return records.map(this::mapToResponse);
    }

    // New Delete Logic
    @Transactional
    public void deleteRecord(Long userId, Long recordId) {
        FinancialRecord record = recordRepository.findByIdAndUserId(recordId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found or you do not have permission to delete it."));

        recordRepository.delete(record); // Triggers soft delete
    }

    public DashboardSummaryResponse getDashboardSummary(Long userId) {
        BigDecimal totalIncome = recordRepository.sumAmountByUserIdAndType(userId, TransactionType.INCOME);
        BigDecimal totalExpense = recordRepository.sumAmountByUserIdAndType(userId, TransactionType.EXPENSE);

        totalIncome = totalIncome != null ? totalIncome : BigDecimal.ZERO;
        totalExpense = totalExpense != null ? totalExpense : BigDecimal.ZERO;
        BigDecimal netBalance = totalIncome.subtract(totalExpense);

        List<Object[]> categoryData = recordRepository.sumAmountByUserIdAndTypeGroupByCategory(userId, TransactionType.EXPENSE);
        Map<String, BigDecimal> categoryTotals = categoryData.stream()
                .collect(Collectors.toMap(obj -> (String) obj[0], obj -> (BigDecimal) obj[1]));

        return new DashboardSummaryResponse(totalIncome, totalExpense, netBalance, categoryTotals);
    }

    private FinancialRecordResponse mapToResponse(FinancialRecord record) {
        return new FinancialRecordResponse(
                record.getId(),
                record.getAmount(),
                record.getType(),
                record.getCategory(),
                record.getDate(),
                record.getNotes(),
                record.getCreatedAt()
        );
    }
}