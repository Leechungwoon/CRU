package com.example.cru.domain.order.controller;

import com.example.cru.common.model.CommonResponse;
import com.example.cru.domain.order.model.request.CreateOrderRequest;
import com.example.cru.domain.order.model.response.CreateOrderResponse;
import com.example.cru.domain.order.model.response.GetDetailOrderResponse;
import com.example.cru.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    // 주문 정보 생성
    @PostMapping("order/users/{userId}")
    public ResponseEntity<CommonResponse> createOrder(
            @PathVariable Long userId,
            @RequestBody CreateOrderRequest request
    ) {

        // 비지니스 로직
        CreateOrderResponse response = orderService.orderCreateService(userId, request);

        // Dto 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("주문에 성공하셨습니다.", response));
    }

    // 주문 정보 조회
    @GetMapping("order/get/{userId}")
    public ResponseEntity<CommonResponse> getDetailOrder(
            @PathVariable Long userId
    ) {
        // 비지니스 로직
        GetDetailOrderResponse response = orderService.getDetailOrder(userId);

        //Dto 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("주무 조회에 성공하셨습니다", response));
    }
}
