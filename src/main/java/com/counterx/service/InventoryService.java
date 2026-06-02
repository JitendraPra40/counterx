package com.counterx.service;

import com.counterx.dto.*;

import java.util.List;

public interface InventoryService {

    InventoryResponseDto createInventory(
            InventoryRequestDto requestDto
    );

    InventoryResponseDto updateInventory(
            Long inventoryId,
            InventoryRequestDto requestDto
    );

    void deleteInventory(Long inventoryId);

    InventoryResponseDto getInventoryById(
            Long inventoryId
    );

    List<InventoryResponseDto> getAllInventory();

    InventoryResponseDto addStock(
            StockAddRequestDto requestDto
    );

    InventoryResponseDto reduceStock(
            StockReduceRequestDto requestDto
    );

    List<InventoryResponseDto> getLowStockItems();

    List<InventoryResponseDto> getExpiringItems();

    void saveInventoryHistory(
            Long inventoryId,
            String actionType,
            String remarks
    );

    List<InventoryHistoryResponseDto> getInventoryHistory(Long inventoryId);

    List<InventoryHistoryResponseDto> getAllInventoryHistory();
}