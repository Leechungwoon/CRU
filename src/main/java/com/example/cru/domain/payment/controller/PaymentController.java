package com.example.cru.domain.payment.controller;

import com.example.cru.common.model.CommonResponse;
import com.example.cru.domain.payment.model.PaymentDto;
import com.example.cru.domain.payment.model.request.SuccessPaymentRequest;
import com.example.cru.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    //결제 승인
    @PostMapping("/orders/{orderId}/payments")
    public ResponseEntity<CommonResponse> successPay(@AuthenticationPrincipal Long userId,  @PathVariable Long orderId, @RequestBody SuccessPaymentRequest request) {

        //비지니스 로직
        PaymentDto response = paymentService.successPayment(userId,orderId,request);

        //Dto 반환
        return ResponseEntity.ok(CommonResponse.success("결제가 완료 됐습니다.", response));
    }

    //결제 취소
    @PostMapping("/payments/{paymentId}/cancel")
    public ResponseEntity<CommonResponse> canceledPay(@AuthenticationPrincipal Long userId, @PathVariable Long paymentId) {

        //비지니스 로직
        paymentService.canceledPayment(userId, paymentId);

        //Dto 반환
        return ResponseEntity.ok(CommonResponse.success("결제가 취소되었습니다.", null));
    }

    //결제 조회
    @GetMapping("/orders/{orderId}/payments")
    public ResponseEntity<CommonResponse> getMyPay(@AuthenticationPrincipal Long userId, @PathVariable Long orderId) {

        //비지니스 로직
        PaymentDto response = paymentService.getMyPayment(userId, orderId);

        //Dto 반환
        return ResponseEntity.ok(CommonResponse.success("결제 내역이 조회됐습니다.", response));
    }

}
