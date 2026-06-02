package com.counterx.dto;

import com.counterx.enums.Category;
import com.counterx.enums.UnitType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequestDto {

    @NotBlank(message = "Item name is required")
    @Size(min = 2, max = 255, message = "Item name must be between 2 and 255 characters")
    private String itemName;

    @NotNull(message = "Category is required")
    private Category category;

    @NotNull(message = "Unit type is required")
    private UnitType unitType;

    @NotNull(message = "Available stock is required")
    @PositiveOrZero(message = "Available stock cannot be negative")
    private BigDecimal availableStock;

    @NotNull(message = "Minimum stock is required")
    @PositiveOrZero(message = "Minimum stock cannot be negative")
    private BigDecimal minimumStock;

    @NotNull(message = "Price per unit is required")
    @Positive(message = "Price per unit must be greater than zero")
    private BigDecimal pricePerUnit;

    @NotBlank(message = "Batch number is required")
    @Size(max = 100, message = "Batch number cannot exceed 100 characters")
    private String batchNumber;

    private LocalDate expiryDate;

    @NotNull(message = "Received date is required")
    private LocalDate receivedDate;
}