package com.anand.finance_data_processing_and_access_control.controller;

import com.anand.finance_data_processing_and_access_control.dto.DashboardSummaryResponse;
import com.anand.finance_data_processing_and_access_control.dto.FinancialRecordRequest;
import com.anand.finance_data_processing_and_access_control.dto.FinancialRecordResponse;
import com.anand.finance_data_processing_and_access_control.security.CustomUserDetails;
import com.anand.finance_data_processing_and_access_control.service.FinancialRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/records")
@RequiredArgsConstructor
public class FinancialRecordController {

    private final FinancialRecordService recordService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<FinancialRecordResponse> createRecord(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody FinancialRecordRequest request) {

        FinancialRecordResponse response = recordService.createRecord(currentUser.getId(), request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Updated for Pagination and Search
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    @GetMapping
    public ResponseEntity<Page<FinancialRecordResponse>> getRecords(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        Page<FinancialRecordResponse> responses = recordService.getRecords(currentUser.getId(), keyword, pageable);

        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST', 'VIEWER')")
    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryResponse> getDashboardSummary(
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        DashboardSummaryResponse summary = recordService.getDashboardSummary(currentUser.getId());
        return ResponseEntity.ok(summary);
    }

    // New Delete Endpoint
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long id) {

        recordService.deleteRecord(currentUser.getId(), id);
        return ResponseEntity.noContent().build();
    }
}