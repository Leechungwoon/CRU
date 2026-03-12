package com.example.cru.domain.order.model.request;

import com.example.cru.domain.order_item.model.request.CreatedOrderItemRequest;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Getter
public class CreatedOrderRequest {

    public String deliveryAddress;
    public String deliveryMemo;
    public List<CreatedOrderItemRequest> items;

    public CreatedOrderRequest(String deliveryAddress, String deliveryMemo) {
        this.deliveryAddress = deliveryAddress;
        this.deliveryMemo = deliveryMemo;
    }
}
