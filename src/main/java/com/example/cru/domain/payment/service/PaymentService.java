package com.example.cru.domain.payment.service;

import com.example.cru.common.enums.OrderStatus;
import com.example.cru.common.enums.PaymentStatus;
import com.example.cru.domain.order.entity.Order;
import com.example.cru.domain.order.repository.OrderRepository;
import com.example.cru.domain.payment.entity.Payment;
import com.example.cru.domain.payment.model.PaymentDto;
import com.example.cru.domain.payment.model.request.SuccessPaymentRequest;
import com.example.cru.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    //결제 승인
    @Transactional
    public PaymentDto successPayment(Long orderId, SuccessPaymentRequest request) {

        // 1. 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));
        //2. 결제 여부 확인
        paymentRepository.findByOrderId(orderId).ifPresent(p -> {
            throw new RuntimeException("이미 결제된 주문입니다.");
        });

        //  3.금액 검증
        if (order.getTotalPrice() != request.getAmount()) {
            throw new RuntimeException("결제 금액이 주문 금액과 다릅니다.");
        }

        // 4.Payment 저장
        Payment payment = Payment.builder()
                .order(order)
                .amount(request.getAmount())
                .status(PaymentStatus.SUCCESS)
                .paidAt(LocalDateTime.now())
                .build();
        paymentRepository.save(payment);

        // 5. Order 상태 변화 결제 승인시 되면 PENDING -> PAID 변경
        order.updateStatus(OrderStatus.PAID);

        return PaymentDto.from(payment);
    }

    //결제 취소
    @Transactional
    public void canceledPayment(Long paymentId) {

        // 1. 결제 조회
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("결제 정보를 찾을 수 없습니다,"));

        // 2. 취소 전,후 여부 확인
        if (payment.getStatus() == PaymentStatus.CANCELED) {
            throw new RuntimeException("취소된 결제입니다.");
        }

        // 3. 결제 취소 로직
        payment.cancel();

        // 4. Order 상태 변경 PAID -> CANCELED로 변경
        Order order = payment.getOrder();
        order.updateStatus(OrderStatus.CANCELED);
    }

    //결제 조회
    @Transactional(readOnly = true)
    public PaymentDto getMyPayment(Long orderId) {

        //결제 상품 조회
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("결제 정보를 찾을 수 없습니다."));

        return PaymentDto.from(payment);
    }


}
