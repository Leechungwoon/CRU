package com.example.cru.domain.payment.entity;

import com.example.cru.common.enums.PaymentStatus;
import com.example.cru.domain.order.entity.Order;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

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

    @Column(unique = true)
    private String tossPaymentKey; // 토스 결제 고유 키 (결제 완료 후 저장)

    private String method; // CARD, VIRTUAL_ACCOUNT 등 (토스 응답값 그대로)

    @Column(nullable = false)
    private int amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;
}
