package com.example.cru.domain.payment.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TossPaymentResponse {

    private String paymentKey; // 최종 확정된 결제 키 - DB 저장이며 환불 시 필수 키
    private String orderId; // 주문 Id
    private String status; // 결제 상태
    private int totalAmount; // 실제 승인 금액


}
