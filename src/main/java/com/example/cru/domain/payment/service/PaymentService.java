package com.example.cru.domain.payment.service;

import com.example.cru.domain.order.repository.OrderRepository;
import com.example.cru.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    //결제 승인
    @Transactional
    public void successPayment(){

    }

    //결제 취소
    @Transactional
    public void canceledPayment(){

    }

    //결제 조회
    @Transactional(readOnly = true)
    public void getMyPayment(){

    }


}
