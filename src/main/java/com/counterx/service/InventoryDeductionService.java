package com.counterx.service;

import com.counterx.dto.OrderInventoryDeductionRequestDto;

public interface InventoryDeductionService {

    void deductInventoryForOrder(
            OrderInventoryDeductionRequestDto request
    );
}