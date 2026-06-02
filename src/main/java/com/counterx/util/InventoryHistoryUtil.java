package com.counterx.util;

import com.counterx.entity.Inventory;
import com.counterx.enums.ActionType;
import com.counterx.service.InventoryHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class InventoryHistoryUtil {

    private final InventoryHistoryService historyService;

    public void stockAdded(
            Inventory inventory,
            BigDecimal previous,
            BigDecimal addedQty,
            BigDecimal newQty,
            String remarks) {

        historyService.recordHistory(
                inventory,
                ActionType.STOCK_ADDED,
                previous,
                addedQty,
                newQty,
                remarks
        );
    }

    public void stockReduced(
            Inventory inventory,
            BigDecimal previous,
            BigDecimal reducedQty,
            BigDecimal newQty,
            String remarks) {

        historyService.recordHistory(
                inventory,
                ActionType.STOCK_REDUCED,
                previous,
                reducedQty,
                newQty,
                remarks
        );
    }

    public void orderDeduction(
            Inventory inventory,
            BigDecimal previous,
            BigDecimal deductedQty,
            BigDecimal newQty,
            String remarks) {

        historyService.recordHistory(
                inventory,
                ActionType.ORDER_DEDUCTION,
                previous,
                deductedQty,
                newQty,
                remarks
        );
    }

    public void manualAdjustment(
            Inventory inventory,
            BigDecimal previous,
            BigDecimal adjustedQty,
            BigDecimal newQty,
            String remarks) {

        historyService.recordHistory(
                inventory,
                ActionType.MANUAL_ADJUSTMENT,
                previous,
                adjustedQty,
                newQty,
                remarks
        );
    }

    public void itemExpired(
            Inventory inventory,
            BigDecimal previous,
            BigDecimal expiredQty,
            BigDecimal newQty,
            String remarks) {

        historyService.recordHistory(
                inventory,
                ActionType.ITEM_EXPIRED,
                previous,
                expiredQty,
                newQty,
                remarks
        );
    }
}