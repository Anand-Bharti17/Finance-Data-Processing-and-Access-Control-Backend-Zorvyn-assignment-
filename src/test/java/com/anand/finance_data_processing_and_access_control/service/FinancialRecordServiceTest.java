package com.anand.finance_data_processing_and_access_control.service;

import com.anand.finance_data_processing_and_access_control.dto.FinancialRecordRequest;
import com.anand.finance_data_processing_and_access_control.dto.FinancialRecordResponse;
import com.anand.finance_data_processing_and_access_control.model.entity.FinancialRecord;
import com.anand.finance_data_processing_and_access_control.model.entity.User;
import com.anand.finance_data_processing_and_access_control.model.enums.TransactionType;
import com.anand.finance_data_processing_and_access_control.repository.FinancialRecordRepository;
import com.anand.finance_data_processing_and_access_control.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FinancialRecordServiceTest {

    @Mock
    private FinancialRecordRepository recordRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FinancialRecordService recordService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");
    }

    @Test
    void createRecord_ShouldReturnResponse_WhenValidDataProvided() {
        // 1. Arrange (Setup mock data)
        FinancialRecordRequest request = new FinancialRecordRequest(
                new BigDecimal("100.00"), TransactionType.INCOME, "Salary", LocalDate.now(), "Test notes"
        );

        FinancialRecord savedRecord = new FinancialRecord();
        savedRecord.setId(10L);
        savedRecord.setAmount(request.amount());
        savedRecord.setType(request.type());
        savedRecord.setCategory(request.category());

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(recordRepository.save(any(FinancialRecord.class))).thenReturn(savedRecord);

        // 2. Act (Call the method)
        FinancialRecordResponse response = recordService.createRecord(1L, request);

        // 3. Assert (Verify the results)
        assertNotNull(response);
        assertEquals(10L, response.id());
        assertEquals(new BigDecimal("100.00"), response.amount());
        assertEquals("Salary", response.category());
        verify(recordRepository, times(1)).save(any(FinancialRecord.class)); // Ensure save was called exactly once
    }
}