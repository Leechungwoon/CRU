package com.example.cru.domain.order_item.model.request;

import com.example.cru.domain.order_item_custom.model.request.OrderItemCustomRequest;
import lombok.Getter;

@Getter
public class OrderItemRequest {

    private Long itemId;
    private int itemPrice;
    private int quantity;
    private OrderItemCustomRequest custom;
}
