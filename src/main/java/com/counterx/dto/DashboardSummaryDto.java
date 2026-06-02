package com.counterx.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryDto {

    private BigDecimal currentInventoryValue;

    private Long lowStockCount;

    private Long outOfStockCount;

    private Long expiringProductsCount;
}