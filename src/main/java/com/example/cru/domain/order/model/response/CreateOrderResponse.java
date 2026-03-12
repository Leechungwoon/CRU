package com.example.cru.domain.order.model.response;

import com.example.cru.common.enums.OrderStatus;
import com.example.cru.domain.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CreateOrderResponse {

    private final Long id;
    private final OrderStatus status;
    private final int totalPrice;
    private final String deliveryAddress;
    private final String deliveryMemo;
    private final LocalDateTime createdAt;

    public static CreateOrderResponse from(Order order) {
        return  new CreateOrderResponse(
                order.getId(),
                order.getStatus(),
                order.getTotalPrice(),
                order.getDeliveryAddress(),
                order.getDeliveryMemo(),
                order.getCreatedAt()
        );
    }
}
