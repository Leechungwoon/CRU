package com.example.cru.domain.item.entity;

import com.example.cru.common.entity.BaseEntity;
import com.example.cru.common.enums.ItemType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("is_deleted = false")
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int basePrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItemType type; // TOP, BOTTOM

    private String imageUrl;

    @Column(nullable = false)
    private boolean isActive = true;

    @Builder
    public Item(Long id, String name, int basePrice, ItemType type, String imageUrl, boolean isActive) {
        this.id = id;
        this.name = name;
        this.basePrice = basePrice;
        this.type = type;
        this.imageUrl = imageUrl;
        this.isActive = isActive;
    }
}