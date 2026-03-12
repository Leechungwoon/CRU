package com.example.cru.domain.order.controller;

import com.example.cru.common.model.CommonResponse;
import com.example.cru.domain.order.model.request.CreatedOrderRequest;
import com.example.cru.domain.order.model.response.CreateOrderResponse;
import com.example.cru.domain.order.service.OrderService;
import com.example.cru.domain.order_item.model.request.OrderItemRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class OrderController {
}
