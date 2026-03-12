package com.example.cru.domain.order_item.model.request;

import com.example.cru.domain.order_item_custom.model.request.OrderItemCustomRequest;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreatedOrderItemRequest {
    private Long itemId;
    private int quantity;
    private int itemPrice;
    private int unitPrice;
    private OrderItemCustomRequest customRequest;
}
