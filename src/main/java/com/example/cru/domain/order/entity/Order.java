package com.example.cru.domain.order.entity;

import com.example.cru.common.entity.BaseEntity;
import com.example.cru.common.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "orders") // order는 SQL 예약어라 복수형 사용
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("is_deleted = false")
public class Order extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private int totalPrice;

    private String deliveryAddress;

    private String deliveryMemo;

    @Builder
    public Order(Long userId, OrderStatus status, int totalPrice, String deliveryAddress, String deliveryMemo) {
        this.userId = userId;
        this.status = status;
        this.totalPrice = totalPrice;
        this.deliveryAddress = deliveryAddress;
        this.deliveryMemo = deliveryMemo;
    }

    public void updateStatus(OrderStatus status) {
        this.status = status;
    }

    public boolean isCancelable() {
        return this.status == OrderStatus.PENDING;
    }

}
