package com.counterx.service.impl;

import com.counterx.entity.Inventory;
import com.counterx.entity.InventoryHistory;
import com.counterx.enums.ActionType;
import com.counterx.repository.InventoryHistoryRepository;
import com.counterx.service.InventoryHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryHistoryServiceImpl
        implements InventoryHistoryService {

    private final InventoryHistoryRepository historyRepository;

    @Override
    public void recordHistory(
            Inventory inventory,
            ActionType actionType,
            BigDecimal previousQuantity,
            BigDecimal changedQuantity,
            BigDecimal newQuantity,
            String remarks) {

        InventoryHistory history =
                InventoryHistory.builder()
                        .inventory(inventory)
                        .actionType(actionType)
                        .previousQuantity(previousQuantity)
                        .changedQuantity(changedQuantity)
                        .newQuantity(newQuantity)
                        .remarks(remarks)
                        .createdBy("SYSTEM")
                        .build();

        historyRepository.save(history);

        log.info(
                "Inventory history created. InventoryId={}, Action={}",
                inventory.getInventoryId(),
                actionType
        );
    }
}