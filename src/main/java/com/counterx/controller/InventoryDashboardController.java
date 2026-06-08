package com.counterx.controller;

import com.counterx.dto.*;
import com.counterx.payload.ApiResponse;
import com.counterx.service.InventoryDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory Dashboard", description = "Inventory dashboard and consumption report APIs")
public class InventoryDashboardController {

    private final InventoryDashboardService dashboardService;

    @GetMapping("/summary")
    @Operation(summary = "Get inventory dashboard summary")
    public ApiResponse<List<DashboardSummaryDto>> summary() {

        return ApiResponse.<List<DashboardSummaryDto>>builder()
                .success(true)
                .message("Dashboard summary fetched")
                .data(
                        dashboardService
                                .getDashboardSummary())
                .timestamp(LocalDateTime.now())
                .build();
    }

}
