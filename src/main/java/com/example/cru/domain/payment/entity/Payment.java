package com.example.cru.domain.payment.entity;

import com.example.cru.common.enums.PaymentStatus;
import com.example.cru.domain.order.entity.Order;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 단방향 @OneToOne — Payment가 Order를 알고, Order는 Payment를 모름
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Column(name = "payment_key", nullable = false, unique = true) //Toss 발급 키 - 환불 시 필수
    private String paymentKey; //unique로 중복 저장 자체를 DB 레벨에서 차단

    @Column(nullable = false)
    private int amount; // 실제 결제 금액

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status; //결제 상태 (SUCCESS / CANCELED)

    @Column(name = "paid_at")
    private LocalDateTime paidAt; //결제 시각

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt; // 취소 시각

    // 결제 취소
    public void cancel() {
        this.status = PaymentStatus.CANCELED; //상태 변경
        this.canceledAt = LocalDateTime.now(); //취소 시각 기록
    }
    //결제 상태가 성공 및 취소만 있는 이유는 결제 MVP에서는 승인 실패 시 payment에 저장을 안하기 때문에 예외로 대체로 진행 추후 PG 연동 후 FAILED 상태 추가 예정
}
