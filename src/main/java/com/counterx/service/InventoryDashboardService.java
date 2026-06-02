package com.counterx.service;

import com.counterx.dto.*;

import java.util.List;

public interface InventoryDashboardService {

    DashboardSummaryDto getDashboardSummary();

    List<ConsumptionReportDto> getDailyConsumption();

    List<ConsumptionReportDto> getWeeklyConsumption();

    List<ConsumptionReportDto> getMonthlyConsumption();

    List<TopConsumedItemDto> getTopConsumedItems();
}