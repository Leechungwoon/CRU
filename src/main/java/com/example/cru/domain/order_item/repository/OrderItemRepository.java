package com.example.cru.domain.order_item.repository;

import com.example.cru.domain.order_item.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
