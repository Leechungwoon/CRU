package com.example.cru.domain.item.model;

import com.example.cru.common.enums.ItemType;
import lombok.Getter;

@Getter
public class CreateItemRequest {

    private String name;
    private int basePrice;
    private ItemType itemType; //TOP, BOTTOM
    private String imageUrl;
}
