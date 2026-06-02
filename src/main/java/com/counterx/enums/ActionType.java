package com.counterx.enums;

/**
 * Inventory movement actions tracked in inventory history.
 */
public enum ActionType {

    STOCK_ADDED,
    STOCK_REDUCED,
    ORDER_DEDUCTION,
    MANUAL_ADJUSTMENT,
    ITEM_EXPIRED,
    LOW_STOCK_ALERT,
    INVENTORY_CREATED,
    INVENTORY_UPDATED,
    INVENTORY_DELETED
}
