package com.example.cru.domain.order.model.request;

import com.example.cru.domain.order_item.model.request.CreateOrderItemRequest;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateOrderRequest {

    public String deliveryAddress;
    public String deliveryMemo;
    public List<CreateOrderItemRequest> items;

    public CreateOrderRequest(String deliveryAddress, String deliveryMemo) {
        this.deliveryAddress = deliveryAddress;
        this.deliveryMemo = deliveryMemo;
    }
}
