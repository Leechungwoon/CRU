package com.example.cru.domain.order_item_custom;

import com.example.cru.domain.order_item_custom.entity.OrderItemCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// OrderService 주문 취소 로직에 사용
public interface OrderItemCustomRepository extends JpaRepository<OrderItemCustom, Long> {
    Optional<OrderItemCustom>findByOrderItemId(Long id);
}
