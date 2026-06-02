package com.counterx.service.impl;

import com.counterx.dto.*;
import com.counterx.entity.Inventory;
import com.counterx.entity.InventoryHistory;
import com.counterx.enums.StockStatus;
import com.counterx.exception.ResourceNotFoundException;
import com.counterx.repository.InventoryHistoryRepository;
import com.counterx.repository.InventoryRepository;
import com.counterx.service.InventoryService;
import com.counterx.util.InventoryHistoryUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryHistoryRepository historyRepository;
    private final InventoryHistoryUtil historyUtil;

    @Override
    public InventoryResponseDto createInventory(
            InventoryRequestDto dto) {

        log.info("Creating inventory item : {}", dto.getItemName());

        if (inventoryRepository.existsByBatchNumberAndDeletedFalse(
                dto.getBatchNumber())) {

            throw new IllegalArgumentException(
                    "Batch number already exists");
        }

        Inventory inventory = Inventory.builder()
                .itemName(dto.getItemName())
                .category(dto.getCategory())
                .unitType(dto.getUnitType())
                .availableStock(dto.getAvailableStock())
                .minimumStock(dto.getMinimumStock())
                .pricePerUnit(dto.getPricePerUnit())
                .batchNumber(dto.getBatchNumber())
                .expiryDate(dto.getExpiryDate())
                .receivedDate(dto.getReceivedDate())
                .stockStatus(calculateStockStatus(
                        dto.getAvailableStock(),
                        dto.getMinimumStock()))
                .createdBy("SYSTEM")
                .updatedBy("SYSTEM")
                .build();

        Inventory saved =
                inventoryRepository.save(inventory);

        historyUtil.manualAdjustment(
                saved,
                BigDecimal.ZERO,
                saved.getAvailableStock(),
                saved.getAvailableStock(),
                "Inventory Created"
        );

        return mapToResponse(saved);
    }

    @Override
    public InventoryResponseDto updateInventory(
            Long inventoryId,
            InventoryRequestDto dto) {

        log.info("Updating inventory : {}", inventoryId);

        Inventory inventory =
                inventoryRepository
                        .findByInventoryIdAndDeletedFalse(
                                inventoryId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Inventory not found"));

        inventory.setItemName(dto.getItemName());
        inventory.setCategory(dto.getCategory());
        inventory.setUnitType(dto.getUnitType());
        inventory.setMinimumStock(dto.getMinimumStock());
        inventory.setPricePerUnit(dto.getPricePerUnit());
        inventory.setExpiryDate(dto.getExpiryDate());
        inventory.setReceivedDate(dto.getReceivedDate());

        inventory.setStockStatus(
                calculateStockStatus(
                        inventory.getAvailableStock(),
                        inventory.getMinimumStock()));

        Inventory saved =
                inventoryRepository.save(inventory);

        historyUtil.manualAdjustment(
                saved,
                saved.getAvailableStock(),
                BigDecimal.ZERO,
                saved.getAvailableStock(),
                "Inventory Updated"
        );

        return mapToResponse(saved);
    }

    @Override
    public void deleteInventory(Long inventoryId) {

        log.info("Deleting inventory : {}", inventoryId);

        Inventory inventory =
                inventoryRepository
                        .findByInventoryIdAndDeletedFalse(
                                inventoryId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Inventory not found"));

        inventory.setDeleted(true);

        inventoryRepository.save(inventory);

        historyUtil.manualAdjustment(
                inventory,
                inventory.getAvailableStock(),
                BigDecimal.ZERO,
                inventory.getAvailableStock(),
                "Inventory Soft Deleted"
        );
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryResponseDto getInventoryById(
            Long inventoryId) {

        Inventory inventory =
                inventoryRepository
                        .findByInventoryIdAndDeletedFalse(
                                inventoryId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Inventory not found"));

        return mapToResponse(inventory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryResponseDto> getAllInventory() {

        return inventoryRepository
                .findByDeletedFalse()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public InventoryResponseDto addStock(
            StockAddRequestDto dto) {

        log.info("Adding stock for inventory : {}",
                dto.getInventoryId());

        Inventory inventory =
                inventoryRepository
                        .findByInventoryIdAndDeletedFalse(
                                dto.getInventoryId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Inventory not found"));

        BigDecimal previous =
                inventory.getAvailableStock();

        BigDecimal updated =
                previous.add(dto.getQuantity());

        inventory.setAvailableStock(updated);

        inventory.setStockStatus(
                calculateStockStatus(
                        updated,
                        inventory.getMinimumStock()));

        Inventory saved =
                inventoryRepository.save(inventory);

        historyUtil.stockAdded(
                saved,
                previous,
                dto.getQuantity(),
                updated,
                dto.getRemarks()
        );

        return mapToResponse(saved);
    }

    @Override
    public InventoryResponseDto reduceStock(
            StockReduceRequestDto dto) {

        log.info("Reducing stock for inventory : {}",
                dto.getInventoryId());

        Inventory inventory =
                inventoryRepository
                        .findByInventoryIdAndDeletedFalse(
                                dto.getInventoryId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Inventory not found"));

        if (inventory.getAvailableStock()
                .compareTo(dto.getQuantity()) < 0) {

            throw new RuntimeException(
                    "Insufficient stock available");
        }

        BigDecimal previous =
                inventory.getAvailableStock();

        BigDecimal updated =
                previous.subtract(dto.getQuantity());

        inventory.setAvailableStock(updated);

        inventory.setStockStatus(
                calculateStockStatus(
                        updated,
                        inventory.getMinimumStock()));

        Inventory saved =
                inventoryRepository.save(inventory);

        historyUtil.stockReduced(
                saved,
                previous,
                dto.getQuantity(),
                updated,
                dto.getRemarks()
        );

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryResponseDto> getLowStockItems() {

        return inventoryRepository
                .findLowStockItems()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryResponseDto> getExpiringItems() {

        return inventoryRepository
                .findExpiringItems(
                        LocalDate.now().plusDays(7))
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void saveInventoryHistory(
            Long inventoryId,
            String actionType,
            String remarks) {

        Inventory inventory =
                inventoryRepository
                        .findByInventoryIdAndDeletedFalse(
                                inventoryId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Inventory not found"));

        historyUtil.manualAdjustment(
                inventory,
                inventory.getAvailableStock(),
                BigDecimal.ZERO,
                inventory.getAvailableStock(),
                remarks
        );
    }

//    private void createHistory(
//            Inventory inventory,
//            ActionType actionType,
//            BigDecimal previousQuantity,
//            BigDecimal changedQuantity,
//            BigDecimal newQuantity,
//            String remarks) {
//
//        InventoryHistory history =
//                InventoryHistory.builder()
//                        .inventory(inventory)
//                        .actionType(actionType)
//                        .previousQuantity(previousQuantity)
//                        .changedQuantity(changedQuantity)
//                        .newQuantity(newQuantity)
//                        .remarks(remarks)
//                        .createdBy("SYSTEM")
//                        .build();
//
//        historyRepository.save(history);
//    }

    private StockStatus calculateStockStatus(
            BigDecimal available,
            BigDecimal minimum) {

        if (available.compareTo(BigDecimal.ZERO) <= 0) {
            return StockStatus.OUT_OF_STOCK;
        }

        if (available.compareTo(minimum) <= 0) {
            return StockStatus.LOW_STOCK;
        }

        return StockStatus.IN_STOCK;
    }

    private InventoryResponseDto mapToResponse(
            Inventory inventory) {

        return InventoryResponseDto.builder()
                .inventoryId(inventory.getInventoryId())
                .itemName(inventory.getItemName())
                .category(inventory.getCategory())
                .unitType(inventory.getUnitType())
                .availableStock(inventory.getAvailableStock())
                .minimumStock(inventory.getMinimumStock())
                .pricePerUnit(inventory.getPricePerUnit())
                .batchNumber(inventory.getBatchNumber())
                .expiryDate(inventory.getExpiryDate())
                .receivedDate(inventory.getReceivedDate())
                .stockStatus(inventory.getStockStatus())
                .deleted(inventory.getDeleted())
                .version(inventory.getVersion())
                .createdAt(inventory.getCreatedAt())
                .createdBy(inventory.getCreatedBy())
                .updatedAt(inventory.getUpdatedAt())
                .updatedBy(inventory.getUpdatedBy())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryHistoryResponseDto> getInventoryHistory(
            Long inventoryId) {

        Inventory inventory = inventoryRepository
                .findByInventoryIdAndDeletedFalse(inventoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Inventory not found : " + inventoryId));

        return historyRepository
                .findByInventoryInventoryIdOrderByCreatedAtDesc(
                        inventory.getInventoryId())
                .stream()
                .map(this::mapHistoryResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryHistoryResponseDto>
    getAllInventoryHistory() {

        return historyRepository
                .findAll()
                .stream()
                .sorted((h1, h2) ->
                        h2.getCreatedAt()
                                .compareTo(h1.getCreatedAt()))
                .map(this::mapHistoryResponse)
                .toList();
    }

    private InventoryHistoryResponseDto mapHistoryResponse(
            InventoryHistory history) {

        return InventoryHistoryResponseDto.builder()
                .historyId(history.getHistoryId())
                .inventoryId(
                        history.getInventory()
                                .getInventoryId())
                .itemName(
                        history.getInventory()
                                .getItemName())
                .actionType(history.getActionType())
                .previousQuantity(
                        history.getPreviousQuantity())
                .changedQuantity(
                        history.getChangedQuantity())
                .newQuantity(
                        history.getNewQuantity())
                .remarks(history.getRemarks())
                .createdAt(history.getCreatedAt())
                .createdBy(history.getCreatedBy())
                .build();
    }
}