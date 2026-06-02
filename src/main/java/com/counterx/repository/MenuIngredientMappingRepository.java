package com.counterx.repository;

import com.counterx.entity.MenuIngredientMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuIngredientMappingRepository
        extends JpaRepository<MenuIngredientMapping, Long> {

    /**
     * Find mappings by menu item
     */
    List<MenuIngredientMapping>
    findByMenuItemNameAndDeletedFalse(String menuItemName);

    /**
     * Find mappings by menu item (ignore case)
     */
    List<MenuIngredientMapping>
    findByMenuItemNameIgnoreCaseAndDeletedFalse(String menuItemName);

    /**
     * Find mappings by inventory item
     */
    List<MenuIngredientMapping>
    findByInventoryInventoryIdAndDeletedFalse(Long inventoryId);

    /**
     * Active mappings
     */
    List<MenuIngredientMapping> findByDeletedFalse();

    /**
     * Menu item existence check
     */
    boolean existsByMenuItemNameIgnoreCaseAndDeletedFalse(
            String menuItemName
    );

    /**
     * Distinct menu items
     */
    @Query("""
            SELECT DISTINCT m.menuItemName
            FROM MenuIngredientMapping m
            WHERE m.deleted = false
            ORDER BY m.menuItemName
            """)
    List<String> findAllMenuItems();

    /**
     * Fetch menu recipe with ingredients
     */
    @Query("""
            SELECT m
            FROM MenuIngredientMapping m
            JOIN FETCH m.inventory
            WHERE m.deleted = false
            AND LOWER(m.menuItemName) = LOWER(:menuItemName)
            """)
    List<MenuIngredientMapping> findRecipeByMenuItem(
            String menuItemName
    );
}