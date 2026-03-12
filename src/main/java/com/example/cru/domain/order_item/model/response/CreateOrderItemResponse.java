package com.example.cru.domain.order_item.model.response;

import com.example.cru.domain.order_item_custom.model.response.OrderItemCustomResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateOrderItemResponse {

    private Long id;
    private Long itemId;
    private int quantity;
    private int uniPrice;
    private int usbTotal;
    private OrderItemCustomResponse customResponse;
}
