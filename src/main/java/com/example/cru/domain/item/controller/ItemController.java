package com.example.cru.domain.item.controller;

import com.example.cru.common.model.CommonResponse;
import com.example.cru.domain.item.model.CreateItemRequest;
import com.example.cru.domain.item.model.CreateItemResponse;
import com.example.cru.domain.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    //item 등록
    @PostMapping("/item/{item}")
    public ResponseEntity<CommonResponse> createItem(@PathVariable Long userId, @PathVariable Long itemId, @RequestBody CreateItemRequest request) {

        //비지니스 로직
        CreateItemResponse response = itemService.createItem(userId, itemId, request);

        //Dto 반환
        return ResponseEntity.ok(CommonResponse.success("상품을 등록했습니다.", response));
    }
}
