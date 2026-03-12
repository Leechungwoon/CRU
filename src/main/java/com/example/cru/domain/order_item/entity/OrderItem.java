package com.example.cru.domain.order_item.entity;

import com.example.cru.common.entity.BaseEntity;
import com.example.cru.common.enums.OrderStatus;
import com.example.cru.domain.item.entity.Item;
import com.example.cru.domain.order.entity.Order;
import com.example.cru.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "order_items") // order는 SQL 예약어라 복수형 사용
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("is_deleted = false")
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    // 단방향 @ManyToOne — OrderItem이 Order를 알고, Order는 OrderItem을 모름
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // 단방향 @ManyToOne — OrderItem이 Item을 알고, Item은 OrderItem을 모름
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int unitPrice; // 주문 시점 가격 스냅샷 (Item.basePrice 변경에 영향 안 받음)

    @Builder
    public OrderItem(Order order, Item item, int quantity, int unitPrice) {
        this.order = order;
        this.item = item;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
}
