package com.counterx.scheduler;

import com.counterx.entity.Inventory;
import com.counterx.enums.StockStatus;
import com.counterx.repository.InventoryRepository;
import com.counterx.util.InventoryHistoryUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryExpiryScheduler {

    private final InventoryRepository inventoryRepository;
    private final InventoryHistoryUtil historyUtil;

    @Scheduled(cron = "0 0 2 * * ?")
    public void processExpiredItems() {

        List<Inventory> items =
                inventoryRepository.findExpiringItems(
                        LocalDate.now());

        for (Inventory inventory : items) {

            BigDecimal previous =
                    inventory.getAvailableStock();

            inventory.setAvailableStock(
                    BigDecimal.ZERO);

            inventory.setStockStatus(
                    StockStatus.EXPIRED);

            inventoryRepository.save(inventory);

            historyUtil.itemExpired(
                    inventory,
                    previous,
                    previous,
                    BigDecimal.ZERO,
                    "Item expired"
            );

            log.info(
                    "Expired item processed : {}",
                    inventory.getItemName()
            );
        }
    }
}