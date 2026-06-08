package com.counterx.controller;

import com.counterx.dto.*;
import com.counterx.payload.ApiResponse;
import com.counterx.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory", description = "Inventory, stock, history, and alert APIs")
public class InventoryController {

    private final InventoryService inventoryService;

    // ==================================================
    // Inventory APIs
    // ==================================================

    @PostMapping
    @Operation(summary = "Create inventory item")
    public ResponseEntity<ApiResponse<InventoryResponseDto>>
    createInventory(
            @Valid
            @RequestBody
            InventoryRequestDto request) {

        return ResponseEntity.ok(
                ApiResponse.<InventoryResponseDto>builder()
                        .success(true)
                        .message("Inventory created successfully")
                        .data(
                                inventoryService.createInventory(
                                        request))
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping
    @Operation(summary = "Get all active inventory items")
    public ResponseEntity<ApiResponse<List<InventoryResponseDto>>>
    getAllInventory() {

        return ResponseEntity.ok(
                ApiResponse.<List<InventoryResponseDto>>builder()
                        .success(true)
                        .message("Inventory fetched successfully")
                        .data(
                                inventoryService.getAllInventory())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get inventory item by ID")
    public ResponseEntity<ApiResponse<InventoryResponseDto>>
    getInventoryById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.<InventoryResponseDto>builder()
                        .success(true)
                        .message("Inventory fetched successfully")
                        .data(
                                inventoryService.getInventoryById(id))
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/{batchNumber}")
    @Operation(summary = "get the items by batchNumber")
    public ResponseEntity<ApiResponse<InventoryResponseDto>> getInventoryByBatch(
            @PathVariable String batchNumber){
        return ResponseEntity.ok(
                ApiResponse.<InventoryResponseDto>builder()
                        .success(true)
                        .message("Inventory fetched successfully")
                        .data(inventoryService.getInventoryByBatchNumber(batchNumber))
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update inventory item")
    public ResponseEntity<ApiResponse<InventoryResponseDto>>
    updateInventory(
            @PathVariable Long id,
            @Valid
            @RequestBody
            InventoryRequestDto request) {

        return ResponseEntity.ok(
                ApiResponse.<InventoryResponseDto>builder()
                        .success(true)
                        .message("Inventory updated successfully")
                        .data(
                                inventoryService.updateInventory(
                                        id,
                                        request))
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete inventory item")
    public ResponseEntity<ApiResponse<String>>
    deleteInventory(
            @PathVariable Long id) {

        inventoryService.deleteInventory(id);

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Inventory deleted successfully")
                        .data("Deleted")
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    // ==================================================
    // Stock APIs
    // ==================================================

    @PostMapping("/add-stock")
    @Operation(summary = "Add stock to an inventory item")
    public ResponseEntity<ApiResponse<InventoryResponseDto>>
    addStock(
            @Valid
            @RequestBody
            StockAddRequestDto request) {

        return ResponseEntity.ok(
                ApiResponse.<InventoryResponseDto>builder()
                        .success(true)
                        .message("Stock added successfully")
                        .data(
                                inventoryService.addStock(
                                        request))
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PostMapping("/reduce-stock")
    @Operation(summary = "Reduce stock from an inventory item")
    public ResponseEntity<ApiResponse<InventoryResponseDto>>
    reduceStock(
            @Valid
            @RequestBody
            StockReduceRequestDto request) {

        return ResponseEntity.ok(
                ApiResponse.<InventoryResponseDto>builder()
                        .success(true)
                        .message("Stock reduced successfully")
                        .data(
                                inventoryService.reduceStock(
                                        request))
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    // ==================================================
    // History APIs
    // ==================================================

    @GetMapping("/history")
    @Operation(summary = "Get all inventory history")
    public ResponseEntity<
            ApiResponse<List<InventoryHistoryResponseDto>>>
    getAllHistory() {

        return ResponseEntity.ok(
                ApiResponse
                        .<List<InventoryHistoryResponseDto>>builder()
                        .success(true)
                        .message("History fetched successfully")
                        .data(
                                inventoryService
                                        .getAllInventoryHistory())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/history/{inventoryId}")
    @Operation(summary = "Get history for an inventory item")
    public ResponseEntity<
            ApiResponse<List<InventoryHistoryResponseDto>>>
    getInventoryHistory(
            @PathVariable Long inventoryId) {

        return ResponseEntity.ok(
                ApiResponse
                        .<List<InventoryHistoryResponseDto>>builder()
                        .success(true)
                        .message("History fetched successfully")
                        .data(
                                inventoryService
                                        .getInventoryHistory(
                                                inventoryId))
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    // ==================================================
    // Alert APIs
    // ==================================================

    @GetMapping("/low-stock")
    @Operation(summary = "Get low-stock inventory items")
    public ResponseEntity<
            ApiResponse<List<InventoryResponseDto>>>
    getLowStockItems() {

        return ResponseEntity.ok(
                ApiResponse
                        .<List<InventoryResponseDto>>builder()
                        .success(true)
                        .message("Low stock items fetched")
                        .data(
                                inventoryService
                                        .getLowStockItems())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/expiring-items")
    @Operation(summary = "Get expiring inventory items")
    public ResponseEntity<
            ApiResponse<List<InventoryResponseDto>>>
    getExpiringItems() {

        return ResponseEntity.ok(
                ApiResponse
                        .<List<InventoryResponseDto>>builder()
                        .success(true)
                        .message("Expiring items fetched")
                        .data(
                                inventoryService
                                        .getExpiringItems())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}
