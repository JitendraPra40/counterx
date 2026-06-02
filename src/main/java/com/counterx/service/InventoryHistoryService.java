package com.counterx.service;

import com.counterx.entity.Inventory;
import com.counterx.enums.ActionType;

import java.math.BigDecimal;


public interface InventoryHistoryService {

    void recordHistory(
            Inventory inventory,
            ActionType actionType,
            BigDecimal previousQuantity,
            BigDecimal changedQuantity,
            BigDecimal newQuantity,
            String remarks
    );


}