package com.example.cru.domain.payment.model.request;

import lombok.Getter;

@Getter
public class TossPaymentRequest {

    private String paymentKey; // 고객이 결제시 받아온 임시 키
    private String orderId; // DB에 orderId (내부 검증에 사용)
    private int amount; //결제 금액(Toss가 실제 인증된 금액과 비교)

    public TossPaymentRequest(String paymentKey, String orderId, int amount) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.amount = amount;
    }
}
