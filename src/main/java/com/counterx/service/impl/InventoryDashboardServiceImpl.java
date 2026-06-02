package com.counterx.service.impl;

import com.counterx.dto.*;
import com.counterx.enums.StockStatus;
import com.counterx.repository.InventoryHistoryRepository;
import com.counterx.repository.InventoryRepository;
import com.counterx.service.InventoryDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryDashboardServiceImpl
        implements InventoryDashboardService {

    private final InventoryRepository inventoryRepository;
    private final InventoryHistoryRepository historyRepository;

    @Override
    public DashboardSummaryDto getDashboardSummary() {

        return DashboardSummaryDto.builder()
                .currentInventoryValue(
                        inventoryRepository
                                .getCurrentInventoryValue())
                .lowStockCount(
                        inventoryRepository
                                .getLowStockCount())
                .outOfStockCount(
                        inventoryRepository
                                .countByStockStatusAndDeletedFalse(
                                        StockStatus.OUT_OF_STOCK))
                .expiringProductsCount(
                        inventoryRepository
                                .getExpiringProductsCount(
                                        LocalDate.now().plusDays(7)))
                .build();
    }

    @Override
    public List<ConsumptionReportDto> getDailyConsumption() {

        return historyRepository.getDailyConsumption()
                .stream()
                .map(this::mapConsumption)
                .toList();
    }

    @Override
    public List<ConsumptionReportDto> getWeeklyConsumption() {

        return historyRepository
                .getWeeklyConsumption(
                        LocalDateTime.now().minusDays(7))
                .stream()
                .map(this::mapConsumption)
                .toList();
    }

    @Override
    public List<ConsumptionReportDto> getMonthlyConsumption() {

        return historyRepository
                .getMonthlyConsumption(
                        LocalDateTime.now().minusDays(30))
                .stream()
                .map(this::mapConsumption)
                .toList();
    }

    @Override
    public List<TopConsumedItemDto> getTopConsumedItems() {

        return historyRepository
                .getTopConsumedItems()
                .stream()
                .limit(10)
                .map(this::mapTopConsumed)
                .toList();
    }

    private ConsumptionReportDto mapConsumption(
            Object[] row) {

        return ConsumptionReportDto.builder()
                .itemName((String) row[0])
                .consumedQuantity(
                        (java.math.BigDecimal) row[1])
                .build();
    }

    private TopConsumedItemDto mapTopConsumed(
            Object[] row) {

        return TopConsumedItemDto.builder()
                .itemName((String) row[0])
                .totalConsumed(
                        (java.math.BigDecimal) row[1])
                .build();
    }
}