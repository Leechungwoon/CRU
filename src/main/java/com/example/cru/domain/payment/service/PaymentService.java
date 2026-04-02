package com.example.cru.domain.payment.service;

import com.example.cru.common.enums.OrderStatus;
import com.example.cru.common.enums.PaymentStatus;
import com.example.cru.common.exception.CustomException;
import com.example.cru.common.exception.ErrorCode;
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
    public PaymentDto successPayment(Long userId, Long orderId, SuccessPaymentRequest request) {

        // 1. 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        // 2. 본인 주문인지 확인
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("본인 주문만 결제할 수 있습니다.");
        }

        //3. 결제 여부 확인
        paymentRepository.findByOrderId(orderId).ifPresent(p -> {
            throw new RuntimeException("이미 결제된 주문입니다.");
        });

        // 4.금액 검증
        if (order.getTotalPrice() != request.getAmount()) {
            throw new RuntimeException("결제 금액이 주문 금액과 다릅니다.");
        }

        // 5.Payment 저장
        Payment payment = Payment.builder()
                .order(order)
                .amount(request.getAmount())
                .status(PaymentStatus.SUCCESS)
                .paidAt(LocalDateTime.now())
                .build();
        paymentRepository.save(payment);

        // 6. Order 상태 변화 결제 승인시 되면 PENDING -> PAID 변경
        order.updateStatus(OrderStatus.PAID);

        return PaymentDto.from(payment);
    }

    //결제 취소
    @Transactional
    public void canceledPayment(Long userId, Long paymentId) {

        // 1. 결제 조회
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        //2. 결제 본인 검증
        if (!payment.getOrder().getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        // 3. 취소 전,후 여부 확인
        if (payment.getStatus() == PaymentStatus.CANCELED) {
            throw new RuntimeException("취소된 결제입니다.");
        }

        // 4. 결제 취소 로직
        payment.cancel();

        // 5. Order 상태 변경 PAID -> CANCELED로 변경
        Order order = payment.getOrder();
        order.updateStatus(OrderStatus.CANCELED);
    }

    //결제 조회
    @Transactional(readOnly = true)
    public PaymentDto getMyPayment(Long userId, Long orderId) {

        // 1. 결제 상품 조회
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        // 2. 결제 본인 인지 검증
        if (!payment.getOrder().getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        return PaymentDto.from(payment);
    }
}
