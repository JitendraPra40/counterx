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
    public ApiResponse<DashboardSummaryDto> summary() {

        return ApiResponse.<DashboardSummaryDto>builder()
                .success(true)
                .message("Dashboard summary fetched")
                .data(
                        dashboardService
                                .getDashboardSummary())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @GetMapping("/daily-consumption")
    @Operation(summary = "Get daily consumption")
    public ApiResponse<List<ConsumptionReportDto>>
    dailyConsumption() {

        return ApiResponse
                .<List<ConsumptionReportDto>>builder()
                .success(true)
                .message("Daily consumption fetched")
                .data(
                        dashboardService
                                .getDailyConsumption())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @GetMapping("/weekly-consumption")
    @Operation(summary = "Get weekly consumption")
    public ApiResponse<List<ConsumptionReportDto>>
    weeklyConsumption() {

        return ApiResponse
                .<List<ConsumptionReportDto>>builder()
                .success(true)
                .message("Weekly consumption fetched")
                .data(
                        dashboardService
                                .getWeeklyConsumption())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @GetMapping("/monthly-consumption")
    @Operation(summary = "Get monthly consumption")
    public ApiResponse<List<ConsumptionReportDto>>
    monthlyConsumption() {

        return ApiResponse
                .<List<ConsumptionReportDto>>builder()
                .success(true)
                .message("Monthly consumption fetched")
                .data(
                        dashboardService
                                .getMonthlyConsumption())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @GetMapping("/top-consumed-items")
    @Operation(summary = "Get top consumed items")
    public ApiResponse<List<TopConsumedItemDto>>
    topConsumedItems() {

        return ApiResponse
                .<List<TopConsumedItemDto>>builder()
                .success(true)
                .message("Top consumed items fetched")
                .data(
                        dashboardService
                                .getTopConsumedItems())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
