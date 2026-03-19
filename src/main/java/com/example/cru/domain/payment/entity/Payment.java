package com.example.cru.domain.payment.entity;

import com.example.cru.common.enums.PaymentStatus;
import com.example.cru.domain.order.entity.Order;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("is_deleted = false")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 단방향 @OneToOne — Payment가 Order를 알고, Order는 Payment를 모름
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Column(nullable = false)
    private int amount; // 실제 결제 금액

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status; //결제 상태 (SUCCESS / CANCELED)

    @Column(name = "paid_at")
    private LocalDateTime paidAt; //결제 시각

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt; // 취소 시각
}
