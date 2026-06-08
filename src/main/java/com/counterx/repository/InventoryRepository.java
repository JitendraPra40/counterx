package com.counterx.repository;

import com.counterx.entity.Inventory;
import com.counterx.enums.Category;
import com.counterx.enums.StockStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    /**
     * Find active inventory by ID
     */
    Optional<Inventory> findByInventoryIdAndDeletedFalse(Long inventoryId);

    Long countByStockStatusAndDeletedFalse(
            StockStatus stockStatus
    );
    /**
     * Find inventory by item name
     */
    List<Inventory> findByItemNameContainingIgnoreCaseAndDeletedFalse(String itemName);

    /**
     * Find inventory by exact batch number
     */
    List<Inventory> findByBatchNumberAndDeletedFalse(String batchNumber);

    /**
     * Check duplicate batch number
     */
    boolean existsByItemNameAndBatchNumberAndDeletedFalse(
            String itemName,
            String batchNumber);

    /**
     * Find inventory by category
     */
    List<Inventory> findByCategoryAndDeletedFalse(Category category);

    /**
     * Find inventory by stock status
     */
    List<Inventory> findByStockStatusAndDeletedFalse(StockStatus stockStatus);

    /**
     * Get all active inventory
     */
    List<Inventory> findByDeletedFalse();

    /**
     * Find low stock items
     */
    @Query("""
            SELECT i
            FROM Inventory i
            WHERE i.deleted = false
            AND i.availableStock <= i.minimumStock
            """)
    List<Inventory> findLowStockItems();

    /**
     * Find expiring items
     */
    @Query("""
            SELECT i
            FROM Inventory i
            WHERE i.deleted = false
            AND i.expiryDate IS NOT NULL
            AND i.expiryDate <= :expiryDate
            """)
    List<Inventory> findExpiringItems(
            @Param("expiryDate") LocalDate expiryDate
    );

    /**
     * Inventory search
     */
    @Query("""
            SELECT i
            FROM Inventory i
            WHERE i.deleted = false
            AND (
                 LOWER(i.itemName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                 OR LOWER(i.batchNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
            """)
    List<Inventory> searchInventory(
            @Param("keyword") String keyword
    );

    /**
     * Inventory search with limit
     */
    @Query("""
            SELECT i
            FROM Inventory i
            WHERE i.deleted = false
            AND LOWER(i.itemName)
            LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    List<Inventory> searchInventory(
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query("""
SELECT COALESCE(
    SUM(i.availableStock * i.pricePerUnit),
    0
)
FROM Inventory i
WHERE i.deleted = false
""")
    BigDecimal getCurrentInventoryValue();

    @Query("""
SELECT COUNT(i)
FROM Inventory i
WHERE i.deleted = false
AND i.availableStock <= i.minimumStock
""")
    Long getLowStockCount();

    @Query("""
SELECT COUNT(i)
FROM Inventory i
WHERE i.deleted = false
AND i.stockStatus =
com.counterx.enums.StockStatus.OUT_OF_STOCK
""")
    Long getOutOfStockCount();

    @Query("""
SELECT COUNT(i)
FROM Inventory i
WHERE i.deleted = false
AND i.expiryDate <= :expiryDate
""")
    Long getExpiringProductsCount(
            LocalDate expiryDate
    );



}
