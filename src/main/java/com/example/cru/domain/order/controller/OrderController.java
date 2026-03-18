package com.example.cru.domain.order.controller;

import com.example.cru.common.model.CommonResponse;
import com.example.cru.common.model.PageResponse;
import com.example.cru.domain.order.model.request.CreateOrderRequest;
import com.example.cru.domain.order.model.response.CreateOrderResponse;
import com.example.cru.domain.order.model.response.GetDetailOrderResponse;
import com.example.cru.domain.order.model.response.GetOrderListResponse;
import com.example.cru.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<CommonResponse> createOrder(@PathVariable Long userId, @RequestBody CreateOrderRequest request) {

        // 비지니스 로직
        CreateOrderResponse response = orderService.orderCreateService(userId, request);

        // Dto 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("주문에 성공하셨습니다.", response));
    }

    // 주문 정보 조회
    @GetMapping("get/order/{userId}")
    public ResponseEntity<CommonResponse> getDetailOrder(@PathVariable Long orderId, @PathVariable Long userId) {
        // 비지니스 로직
        GetDetailOrderResponse response = orderService.getDetailOrder(orderId, userId);

        //Dto 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("주문 조회에 성공하셨습니다", response));
    }

    //주문 목록 조회 -> 페이징 적용
    @GetMapping("users/orderList")
    public ResponseEntity<PageResponse> getOrderList(@PathVariable Long userId, @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        //페이징 적용된 비지니스 로직
        Page<GetOrderListResponse> responses = orderService.getAllOrder(userId, pageable);

        //Dto 반환
        return ResponseEntity.ok().body(PageResponse.success("주문 목록 조회에 성공하셨습니다", responses));
    }

    @DeleteMapping("/orders/{orderId}/users/{userId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId, @PathVariable Long userId) {

        //비지니스 로직
        orderService.cancelOrder(orderId, userId);

        //삭제 시 메세지 반환
        return ResponseEntity.noContent().build();
    }
}
