package com.counterx.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsumptionReportDto {

    private String itemName;

    private BigDecimal consumedQuantity;
}
