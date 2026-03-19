package com.example.cru.domain.payment.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SuccessPaymentRequest {

    private int amount; // 서버 검증용
}
