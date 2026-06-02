package com.counterx.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderInventoryDeductionRequestDto {

    @NotBlank
    private String menuItemName;

    @NotNull
    @Min(1)
    private Integer orderQuantity;

    private String orderId;
}