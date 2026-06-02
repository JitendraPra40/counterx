package com.counterx.dto;

import com.counterx.enums.ActionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryHistoryResponseDto {

    private Long historyId;

    private Long inventoryId;

    private String itemName;

    private ActionType actionType;

    private BigDecimal previousQuantity;

    private BigDecimal changedQuantity;

    private BigDecimal newQuantity;

    private String remarks;

    private LocalDateTime createdAt;

    private String createdBy;
}