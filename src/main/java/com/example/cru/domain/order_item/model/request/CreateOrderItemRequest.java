package com.example.cru.domain.order_item.model.request;

import com.example.cru.domain.order_item_custom.model.request.OrderItemCustomRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateOrderItemRequest {
    private Long itemId;
    private int quantity;
    private int itemPrice;
    private int unitPrice;
    private OrderItemCustomRequest customRequest;
}
