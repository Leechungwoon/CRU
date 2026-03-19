package com.example.cru.domain.item.model;

import com.example.cru.domain.item.entity.Item;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateItemResponse {

    private Long itemId;
    private String name;
    private int basePrice;

    public static CreateItemResponse from(Item item){
        return CreateItemResponse.builder()
                .itemId(item.getId())
                .name(item.getName())
                .basePrice(item.getBasePrice())
                .build();
    }
}
