package com.counterx.service.impl;

import com.counterx.dto.OrderInventoryDeductionRequestDto;
import com.counterx.entity.Inventory;
import com.counterx.entity.MenuIngredientMapping;
import com.counterx.enums.StockStatus;
import com.counterx.repository.InventoryRepository;
import com.counterx.repository.MenuIngredientMappingRepository;
import com.counterx.service.InventoryDeductionService;
import com.counterx.util.InventoryHistoryUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InventoryDeductionServiceImpl
        implements InventoryDeductionService {

    private final MenuIngredientMappingRepository mappingRepository;
    private final InventoryRepository inventoryRepository;
    private final InventoryHistoryUtil historyUtil;

    @Override
    public void deductInventoryForOrder(
            OrderInventoryDeductionRequestDto request) {

        log.info(
                "Starting inventory deduction for menu={}, qty={}",
                request.getMenuItemName(),
                request.getOrderQuantity()
        );

        List<MenuIngredientMapping> recipe =
                mappingRepository.findRecipeByMenuItem(
                        request.getMenuItemName());

        if (recipe.isEmpty()) {
            throw new RuntimeException(
                    "Menu recipe not configured : "
                            + request.getMenuItemName());
        }

        /*
         * STEP 1
         * Validate entire order first
         */

        validateStockAvailability(
                recipe,
                request.getOrderQuantity()
        );

        /*
         * STEP 2
         * Deduct stock
         */

        processDeduction(
                recipe,
                request.getOrderQuantity(),
                request.getOrderId()
        );

        log.info(
                "Inventory deduction completed for order={}",
                request.getOrderId()
        );
    }

    private void validateStockAvailability(
            List<MenuIngredientMapping> recipe,
            Integer orderQuantity) {

        for (MenuIngredientMapping mapping : recipe) {

            Inventory inventory =
                    mapping.getInventory();

            BigDecimal requiredQuantity =
                    mapping.getRequiredQuantity()
                            .multiply(
                                    BigDecimal.valueOf(
                                            orderQuantity));

            if (inventory.getAvailableStock()
                    .compareTo(requiredQuantity) < 0) {

                throw new RuntimeException(
                        "Insufficient stock for "
                                + inventory.getItemName()
                                + ". Required="
                                + requiredQuantity
                                + ", Available="
                                + inventory.getAvailableStock());
            }
        }
    }

    private void processDeduction(
            List<MenuIngredientMapping> recipe,
            Integer orderQuantity,
            String orderId) {

        for (MenuIngredientMapping mapping : recipe) {

            Inventory inventory =
                    mapping.getInventory();

            BigDecimal requiredQuantity =
                    mapping.getRequiredQuantity()
                            .multiply(
                                    BigDecimal.valueOf(
                                            orderQuantity));

            BigDecimal previousQuantity =
                    inventory.getAvailableStock();

            BigDecimal updatedQuantity =
                    previousQuantity.subtract(
                            requiredQuantity);

            inventory.setAvailableStock(
                    updatedQuantity);

            updateStockStatus(inventory);

            inventoryRepository.save(inventory);

            historyUtil.orderDeduction(
                    inventory,
                    previousQuantity,
                    requiredQuantity,
                    updatedQuantity,
                    "Order Id : " + orderId
            );

            log.info(
                    "Deducted {} {} from {}",
                    requiredQuantity,
                    mapping.getUnitType(),
                    inventory.getItemName()
            );
        }
    }

    private void updateStockStatus(
            Inventory inventory) {

        if (inventory.getAvailableStock()
                .compareTo(BigDecimal.ZERO) <= 0) {

            inventory.setStockStatus(
                    StockStatus.OUT_OF_STOCK);
            return;
        }

        if (inventory.getAvailableStock()
                .compareTo(
                        inventory.getMinimumStock()) <= 0) {

            inventory.setStockStatus(
                    StockStatus.LOW_STOCK);
            return;
        }

        inventory.setStockStatus(
                StockStatus.IN_STOCK);
    }
}