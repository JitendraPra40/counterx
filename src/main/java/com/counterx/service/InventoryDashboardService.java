package com.counterx.service;

import com.counterx.dto.*;
import com.counterx.entity.Inventory;
import com.counterx.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryDashboardService {

    private final InventoryRepository inventoryRepository;

    public List<DashboardSummaryDto> getDashboardSummary() {

        List<Inventory> inventories = inventoryRepository.findAll();
        return inventories.
                stream()
                .map(this::mapToDashboardSummary)
                .toList();
    }

    public DashboardSummaryDto mapToDashboardSummary(Inventory inventory) {
        return DashboardSummaryDto.builder()
                .inventoryId(inventory.getInventoryId())
                .itemName(inventory.getItemName())
                .category(inventory.getCategory())
                .unitType(inventory.getUnitType())
                .availableStock(inventory.getAvailableStock())
                .pricePerUnit(inventory.getPricePerUnit())
                .currentInventoryValue(
                        inventory.getPricePerUnit()
                                .multiply(
                                        inventory.getAvailableStock()))
                .receivedDate(inventory.getReceivedDate())
                .deleted(inventory.getDeleted())
                .createdAt(inventory.getCreatedAt())
                .createdBy(inventory.getCreatedBy())
                .updatedAt(inventory.getUpdatedAt())
                .updatedBy(inventory.getUpdatedBy())
                .build();
    }

}