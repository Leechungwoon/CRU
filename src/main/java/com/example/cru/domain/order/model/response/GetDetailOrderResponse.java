package com.example.cru.domain.order.model.response;

import com.example.cru.common.enums.OrderStatus;
import com.example.cru.domain.order.entity.Order;
import com.example.cru.domain.order_item.entity.OrderItem;
import com.example.cru.domain.order_item.model.response.GetDetailOrderItemResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetDetailOrderResponse {

    private final Long orderId;
    private final OrderStatus status;
    private final int totalPrice;
    private final String deliveryAddress;
    private final String deliveryMemo;
    private final LocalDateTime createdAt;
    private final List<GetDetailOrderItemResponse> item;

    //반환하는 정보의 양이 다르기 때문에

    public static GetDetailOrderResponse from(Order order, List<OrderItem> orderItemList) {
        List<GetDetailOrderItemResponse> itemList = orderItemList.stream()
                .map(GetDetailOrderItemResponse::from)
                .toList();

        return  new GetDetailOrderResponse(
                order.getId(),
                order.getStatus(),
                order.getTotalPrice(),
                order.getDeliveryAddress(),
                order.getDeliveryMemo(),
                order.getCreatedAt(),
                itemList
        );
    }

}
