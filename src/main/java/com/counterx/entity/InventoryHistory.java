package com.counterx.entity;

import com.counterx.enums.ActionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "inventory_history",
        indexes = {
                @Index(name = "idx_history_inventory", columnList = "inventory_id"),
                @Index(name = "idx_history_action", columnList = "action_type"),
                @Index(name = "idx_history_created_at", columnList = "created_at")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "inventory_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_inventory_history_inventory")
    )
    private Inventory inventory;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false, length = 50)
    private ActionType actionType;

    @Column(name = "previous_quantity", nullable = false, precision = 12, scale = 2)
    private BigDecimal previousQuantity;

    @Column(name = "changed_quantity", nullable = false, precision = 12, scale = 2)
    private BigDecimal changedQuantity;

    @Column(name = "new_quantity", nullable = false, precision = 12, scale = 2)
    private BigDecimal newQuantity;

    @Column(name = "remarks", length = 500)
    private String remarks;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}