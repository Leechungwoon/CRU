package com.example.cru.domain.item.service;

import com.example.cru.domain.item.entity.Item;
import com.example.cru.domain.item.model.CreateItemRequest;
import com.example.cru.domain.item.model.CreateItemResponse;
import com.example.cru.domain.item.repository.ItemRepository;
import com.example.cru.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    //item 등록
    @Transactional
    public CreateItemResponse createItem(Long userId, CreateItemRequest request) {

        // 1. item 생성
        Item item = Item.builder()
                .name(request.getName())
                .basePrice(request.getBasePrice())
                .type(request.getItemType())
                .imageUrl(request.getImageUrl())
                .isActive(true)
                .build();
        itemRepository.save(item);
        return CreateItemResponse.from(item);
    }


}
