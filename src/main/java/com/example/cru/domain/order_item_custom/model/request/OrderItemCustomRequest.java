package com.example.cru.domain.order_item_custom.model.request;

import lombok.Getter;

@Getter
public class OrderItemCustomRequest {
    private String playerName;
    private String number;
    private String emblemImagerUrl;
    private boolean patchOption;
    private String sleeveOption;
    private String frontImageUrl;
    private String backImage;
}
