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

    @Version
    private Long version; //낙관적 락 버전 컬럼 -> 저장시 1 증가, 동시 수정 감시용

    @Column(nullable = false)
    private Long userId; //주문자 id

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status; // 주문상태

    @Column(nullable = false)
    private int totalPrice; //총 금액

    private String deliveryAddress; // 배송지

    private String deliveryMemo; // 배송 메모

    @Builder
    public Order(Long userId, OrderStatus status, int totalPrice, String deliveryAddress, String deliveryMemo) {
        this.userId = userId;
        this.status = status;
        this.totalPrice = totalPrice;
        this.deliveryAddress = deliveryAddress;
        this.deliveryMemo = deliveryMemo;
    }

    //주문 상태 변경시 사용
    public void updateStatus(OrderStatus status) {
        this.status = status;
    }

    //주문 취소 시 사용(PENDING)인 경우에만 변경 가능
    public boolean isCancelable() {
        return this.status == OrderStatus.PENDING;
    }

    //결제 진행 가능 여부 - PENDING 상태인 주문만 결제 허용
    public boolean isPayable() {
        return this.status == OrderStatus.PENDING; //PENDING만 결제 가능
    }

}
