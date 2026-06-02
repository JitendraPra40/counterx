package com.counterx.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "inventory",
        indexes = {
                @Index(name = "idx_inventory_item_name", columnList = "item_name"),
                @Index(name = "idx_inventory_category", columnList = "category"),
                @Index(name = "idx_inventory_batch_number", columnList = "batch_number"),
                @Index(name = "idx_inventory_expiry_date", columnList = "expiry_date"),
                @Index(name = "idx_inventory_stock_status", columnList = "stock_status")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Long inventoryId;

    @Column(name = "item_name", nullable = false, length = 255)
    private String itemName;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 50)
    private com.counterx.enums.Category category;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit_type", nullable = false, length = 30)
    private com.counterx.enums.UnitType unitType;

    @Column(name = "available_stock", nullable = false, precision = 12, scale = 2)
    private BigDecimal availableStock;

    @Column(name = "minimum_stock", nullable = false, precision = 12, scale = 2)
    private BigDecimal minimumStock;

    @Column(name = "price_per_unit", nullable = false, precision = 12, scale = 2)
    private BigDecimal pricePerUnit;

    @Column(name = "batch_number", nullable = false, unique = true, length = 100)
    private String batchNumber;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "received_date", nullable = false)
    private LocalDate receivedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "stock_status", nullable = false, length = 30)
    private com.counterx.enums.StockStatus stockStatus;

    @Builder.Default
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    @Version
    @Column(name = "version")
    private Long version;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @OneToMany(
            mappedBy = "inventory",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<InventoryHistory> inventoryHistories = new ArrayList<>();

    @OneToMany(
            mappedBy = "inventory",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<MenuIngredientMapping> ingredientMappings = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (deleted == null) {
            deleted = false;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}