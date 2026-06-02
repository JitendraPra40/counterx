package com.counterx.repository;

import com.counterx.entity.InventoryHistory;
import com.counterx.enums.ActionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface InventoryHistoryRepository
        extends JpaRepository<InventoryHistory, Long> {

    /**
     * Find history by inventory ID
     */
    List<InventoryHistory> findByInventoryInventoryIdOrderByCreatedAtDesc(
            Long inventoryId
    );

    /**
     * Find history by action type
     */
    List<InventoryHistory> findByActionTypeOrderByCreatedAtDesc(
            ActionType actionType
    );

    /**
     * Find history between dates
     */
    List<InventoryHistory> findByCreatedAtBetween(
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    /**
     * Latest history records
     */
    @Query("""
            SELECT h
            FROM InventoryHistory h
            ORDER BY h.createdAt DESC
            """)
    List<InventoryHistory> findLatestHistory();

    /**
     * Inventory consumption history
     */
    @Query("""
            SELECT h
            FROM InventoryHistory h
            WHERE h.inventory.inventoryId = :inventoryId
            AND h.actionType = com.counterx.enums.ActionType.ORDER_DEDUCTION
            ORDER BY h.createdAt DESC
            """)
    List<InventoryHistory> findConsumptionHistory(
            @Param("inventoryId") Long inventoryId
    );

    /**
     * Count actions by type
     */
    long countByActionType(ActionType actionType);


//Daily Consumption
    @Query("""
SELECT h.inventory.itemName,
       SUM(h.changedQuantity)
FROM InventoryHistory h
WHERE h.actionType =
com.counterx.enums.ActionType.ORDER_DEDUCTION
AND DATE(h.createdAt) = CURRENT_DATE
GROUP BY h.inventory.itemName
""")
    List<Object[]> getDailyConsumption();


    //Weekly Consumption
    @Query("""
SELECT h.inventory.itemName,
       SUM(h.changedQuantity)
FROM InventoryHistory h
WHERE h.actionType =
com.counterx.enums.ActionType.ORDER_DEDUCTION
AND h.createdAt >= :startDate
GROUP BY h.inventory.itemName
""")
    List<Object[]> getWeeklyConsumption(
            LocalDateTime startDate
    );

    //Monthly Consumption
    @Query("""
SELECT h.inventory.itemName,
       SUM(h.changedQuantity)
FROM InventoryHistory h
WHERE h.actionType =
com.counterx.enums.ActionType.ORDER_DEDUCTION
AND h.createdAt >= :startDate
GROUP BY h.inventory.itemName
""")
    List<Object[]> getMonthlyConsumption(
            LocalDateTime startDate
    );

    //Top Consumed Items
    @Query("""
SELECT h.inventory.itemName,
       SUM(h.changedQuantity)
FROM InventoryHistory h
WHERE h.actionType =
com.counterx.enums.ActionType.ORDER_DEDUCTION
GROUP BY h.inventory.itemName
ORDER BY SUM(h.changedQuantity) DESC
""")
    List<Object[]> getTopConsumedItems();

}
