package com.example.cru.domain.payment.model;

import com.example.cru.common.enums.PaymentStatus;
import com.example.cru.domain.payment.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class PaymentDto {
    private Long paymentId;
    private int amount;
    private PaymentStatus status;
    private LocalDateTime paidAt;
    private LocalDateTime canceledAt;

    public static PaymentDto from(Payment pay){
        return PaymentDto.builder()
                .paymentId(pay.getId())
                .amount(pay.getAmount())
                .status(pay.getStatus())
                .paidAt(pay.getPaidAt())
                .canceledAt(pay.getCanceledAt())
                .build();
    }
}
