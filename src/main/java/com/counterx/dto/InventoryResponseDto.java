package com.counterx.dto;

import com.counterx.enums.Category;
import com.counterx.enums.StockStatus;
import com.counterx.enums.UnitType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponseDto {

    private Long inventoryId;

    private String itemName;

    private Category category;

    private UnitType unitType;

    private BigDecimal availableStock;

    private BigDecimal minimumStock;

    private BigDecimal pricePerUnit;

    private String batchNumber;

    private LocalDate expiryDate;

    private LocalDate receivedDate;

    private StockStatus stockStatus;

    private Boolean deleted;

    private Long version;

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime updatedAt;

    private String updatedBy;
}