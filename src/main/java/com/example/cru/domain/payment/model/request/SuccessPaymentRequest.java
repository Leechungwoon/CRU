package com.example.cru.domain.payment.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.security.PrivateKey;

@Getter
@AllArgsConstructor
public class SuccessPaymentRequest {

    private String paymentKey; // Toss에서 발급한 임시 결제 키 - Toss 승인 API 호출 시 필수
    private int amount; // 서버 검증용
}
