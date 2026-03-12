package com.example.cru.domain.order.service;

import com.example.cru.domain.item.entity.Item;
import com.example.cru.domain.item.repository.ItemRepository;
import com.example.cru.domain.order.entity.Order;
import com.example.cru.domain.order.model.request.CreateOrderRequest;
import com.example.cru.domain.order.model.response.CreateOrderResponse;
import com.example.cru.domain.order.repository.OrderRepository;
import com.example.cru.domain.order_item.entity.OrderItem;
import com.example.cru.domain.order_item.model.request.CreateOrderItemRequest;
import com.example.cru.domain.order_item.repository.OrderItemRepository;
import com.example.cru.domain.order_item_custom.entity.OrderItemCustom;
import com.example.cru.domain.order_item_custom.model.request.OrderItemCustomRequest;
import com.example.cru.domain.payment.repository.OrderItemCustomRepository;
import com.example.cru.domain.user.entity.User;
import com.example.cru.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemCustomRepository orderItemCustomRepository;
    private final ItemRepository itemRepository;


    //주문 생성
    @Transactional
    public CreateOrderResponse orderCreateService(Long userId, CreateOrderRequest request) {

        //유저(판매자) 조회
        User buyer = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException());


        //총 금액 계산
//        int totalPrice = request.getItems().stream()
//                .mapToInt(item -> item.getItemPrice() * item.getQuantity()).sum();

        //총 금액 계산
        /**
         * 총 금액 계산
         *
         * 주문 요청에는 여러 개의 상품이 포함될 수 있음
         * 각 상품의 가격 * 수량을 계산하여 총 금액을 산출함
         */
        int totalPrice = 0;
        for (CreateOrderItemRequest createOrderItemRequest : request.getItems()) {
            totalPrice += createOrderItemRequest.getItemPrice() * createOrderItemRequest.getQuantity();
        }

        /**
         * Order 엔티티 생성
         *
         * 주문의 기본 정보 저장
         * 주문의 사용자
         * 총 주문 금액
         * 배송 주소
         * 배송시 메모사항
         */
        //order 저장 -> 주문 정보
        Order order = Order.builder()
                .userId(userId)
                .totalPrice(totalPrice)
                .deliveryAddress(request.getDeliveryAddress())
                .deliveryMemo(request.getDeliveryMemo())
                .build();
        orderRepository.save(order);


        /**
         * OrderItem 생성 및 저장
         *
         * 하나의 주문은 여러 개의 주문 상품을 가질 수 있음
         * Order(OrderItem1, OrderItem2, OrderItem3)
         *
         */
        //orderItem + orderItemCustom 저장
        for (CreateOrderItemRequest itemRequest : request.getItems()) {

            //주문 상품 조회 및 실제 존재 상품인지 검증
            Item item = itemRepository.findById(itemRequest.getItemId())
                    .orElseThrow(() -> new RuntimeException());
            //.orElseThrow(() -> new BusinessException(ErrorCode.ITEM_NOT_FOUND));

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .item(item)
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(itemRequest.getUnitPrice())
                    .build();
            orderItemRepository.save(orderItem);

            /**
             * 커스텀 옵션 처리 로직
             *
             * 선수 이름 마킹, 등번호, 엠블럼, 패치 옵션, 이미지 확인
             * 커스텀 요청 있는 경우만 저장
             */
            //커스텀 옵션이 있을 때만 저장
            if (itemRequest.getCustomRequest() != null) {

                // 하나의 주문 상품에 대해 커스텀 옵션을 저장함
                OrderItemCustomRequest c = itemRequest.getCustomRequest();
                orderItemCustomRepository.save(
                        OrderItemCustom.builder()
                                .orderItem(orderItem)
                                .playerName(c.getPlayerName())
                                .number(c.getNumber())
                                .emblemImageUrl(c.getEmblemImagerUrl())
                                .patchOption(c.isPatchOption())
                                .sleeveOption(c.getSleeveOption())
                                .frontImageUrl(c.getFrontImageUrl())
                                .backImageUrl(c.getBackImage())
                                .build()
                );
            }
        }

        // 주문 생성 결과 반환 Order 기반 응답 DTO변환
        return CreateOrderResponse.from(order);
    }
}

