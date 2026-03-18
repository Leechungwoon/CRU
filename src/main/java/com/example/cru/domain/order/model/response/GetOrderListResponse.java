package com.example.cru.domain.order.model.response;

import com.example.cru.common.enums.OrderStatus;
import com.example.cru.domain.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GetOrderListResponse {

    private Long id;
    private OrderStatus status;
    private int totalPrice;
    private LocalDateTime createdAt;

    public static GetOrderListResponse from(Order order) {
        return new GetOrderListResponse(
                order.getId(),
                order.getStatus(),
                order.getTotalPrice(),
                order.getCreatedAt()
        );
    }
}
