package com.example.cru.domain.order_item.model.response;

import com.example.cru.domain.order_item.entity.OrderItem;
import lombok.Getter;

@Getter
public class GetDetailOrderItemResponse {
    private Long id; // OrderItemId
    private Long itemId; // ItemId
    private String itemName; // Item 이름
    private int quantity; //수량
    private int uniPrice; // 주문 시점 가격
    private int subTotal; // 소계 (unitPrice * quantity)

    //생성자
    public GetDetailOrderItemResponse(Long id, Long itemId, String itemName, int quantity, int uniPrice, int subTotal) {
        this.id = id;
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.uniPrice = uniPrice;
        this.subTotal = subTotal;
    }

    //정적 메소드
    public static GetDetailOrderItemResponse from(OrderItem orderItem) {
        return new GetDetailOrderItemResponse(
                orderItem.getId(),
                orderItem.getItem().getId(),
                orderItem.getItem().getName(),
                orderItem.getQuantity(),
                orderItem.getUnitPrice(),
                orderItem.getUnitPrice() * orderItem.getQuantity()
        );
    }
}
