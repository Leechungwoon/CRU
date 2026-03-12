package com.example.cru.domain.order_item_custom.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderItemCustomResponse {
    private String playerName;
    private String number;
    private String emblemImagerUrl;
    private boolean patchOption;
    private String sleeveOption;
    private String frontImageUrl;
    private String backImage;
}
