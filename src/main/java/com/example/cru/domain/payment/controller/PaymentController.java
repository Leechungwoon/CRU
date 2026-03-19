package com.example.cru.domain.payment.controller;

import com.example.cru.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    //결제 승인
    @PostMapping
    public void successPay() {

        //비지니스 로직
        paymentService.successPayment();

    }

    //결제 취소
    @PostMapping
    public void canceledPay() {

        //비지니스 로직
        paymentService.canceledPayment();
    }

    //결제 조회
    @GetMapping
    public void getMyPay() {

        //비지니스 로직
        paymentService.getMyPayment();
    }

}
