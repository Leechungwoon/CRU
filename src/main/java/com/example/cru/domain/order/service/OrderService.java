package com.example.cru.domain.order.service;

import com.example.cru.domain.item.repository.ItemRepository;
import com.example.cru.domain.order.entity.Order;
import com.example.cru.domain.order.model.request.CreatedOrderRequest;
import com.example.cru.domain.order.model.response.CreateOrderResponse;
import com.example.cru.domain.order.repository.OrderRepository;
import com.example.cru.domain.order_item.entity.OrderItem;
import com.example.cru.domain.order_item.model.request.CreatedOrderItemRequest;
import com.example.cru.domain.order_item.model.request.OrderItemRequest;
import com.example.cru.domain.order_item.repository.OrderItemRepository;
import com.example.cru.domain.user.entity.User;
import com.example.cru.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {
}
