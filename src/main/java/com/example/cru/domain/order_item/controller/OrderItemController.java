package com.example.cru.domain.order_item.controller;

import com.example.cru.domain.order_item.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class OrderItemController {
    private final OrderItemService orderService;


}
