package com.example.cru.domain.order.service;

import com.example.cru.common.enums.OrderStatus;
import com.example.cru.common.exception.CustomException;
import com.example.cru.common.exception.ErrorCode;
import com.example.cru.domain.item.entity.Item;
import com.example.cru.domain.item.repository.ItemRepository;
import com.example.cru.domain.order.entity.Order;
import com.example.cru.domain.order.model.request.CreateOrderRequest;
import com.example.cru.domain.order.model.response.CreateOrderResponse;
import com.example.cru.domain.order.model.response.GetDetailOrderResponse;
import com.example.cru.domain.order.model.response.GetOrderListResponse;
import com.example.cru.domain.order.repository.OrderRepository;
import com.example.cru.domain.order_item.entity.OrderItem;
import com.example.cru.domain.order_item.model.request.CreateOrderItemRequest;
import com.example.cru.domain.order_item.repository.OrderItemRepository;
import com.example.cru.domain.order_item_custom.entity.OrderItemCustom;
import com.example.cru.domain.order_item_custom.model.request.OrderItemCustomRequest;
import com.example.cru.domain.order_item_custom.OrderItemCustomRepository;
import com.example.cru.domain.s3.S3Service;
import com.example.cru.domain.user.entity.User;
import com.example.cru.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemCustomRepository orderItemCustomRepository;
    private final ItemRepository itemRepository;
    private final S3Service s3Service;


    //주문 생성
    @Transactional
    public CreateOrderResponse orderCreateService(Long userId, CreateOrderRequest request) {

        //유저(판매자) 조회
        User buyer = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

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
         * Order 저장
         *
         * 주문의 기본 정보 저장
         * 주문의 사용자
         * 총 주문 금액
         * 배송 주소
         * 배송시 메모사항
         * 주문 상태
         */
        //order 저장 -> 주문 정보
        Order order = Order.builder()
                .userId(userId)
                .totalPrice(totalPrice)
                .deliveryAddress(request.getDeliveryAddress())
                .deliveryMemo(request.getDeliveryMemo())
                .status(OrderStatus.PENDING)
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
                    .orElseThrow(() -> new CustomException(ErrorCode.ITEM_NOT_FOUND));

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

    //주문 정보 상세 조회

    /**
     * 주문 정보 상세 조회
     *
     * @param orderId 취소할 주문 ID
     * @param userId  요청한 유저 ID
     * @return 주문 상세 정보 DTO
     */
    @Transactional(readOnly = true)
    public GetDetailOrderResponse getDetailOrder(Long orderId, Long userId) {

        // 1. Order 정보 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        // 2. OrderItem 목록 조회
        List<OrderItem> orderItemList = orderItemRepository.findByOrderId(orderId);

        return GetDetailOrderResponse.from(order, orderItemList);
    }

    // 3. 주문 정보 목록 조회
    @Transactional(readOnly = true)
    public Page<GetOrderListResponse> getAllOrder(Long userId, Pageable pageable) {

        Page<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        return orders.map(GetOrderListResponse::from);
    }

    //주문 취소 로직

    /**
     * 주문 취소 로직
     * PENDING 상태인 주문만 취소 가능
     * CANCEL로 변경 시 S3에 업로드된 이미지 자동 삭제
     *
     * @param orderId 취소할 주문 ID
     * @param userId  요청한 유저 ID
     */
    @Transactional
    public void cancelOrder(Long orderId, Long userId) {

        // 1. 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        // 2. 본인 주문 여부 확인
        if (!order.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        // 3. 취소 기능 상태 확인 PENDING 상태인지 확인
        if (!order.isCancelable()) {
            throw new CustomException(ErrorCode.ORDER_NOT_CANCELABLE);
        }

        // 4. 해당 주문의 OrderItem 목록 조회
        List<OrderItem> orderItemList = orderItemRepository.findByOrderId(orderId);

        // 5. 각 연결된 OrderItem에 연결된 커스텀 이미지 S3에 삭제
        for (OrderItem orderItem : orderItemList) {

            //커스텀 옵션이 있는 경우에만 삭제
            orderItemCustomRepository.findByOrderItemId(orderItem.getId())
                    .ifPresent(custom -> {

                        //고객이 직접 업로드한 엠블럼 삭제
                        if (custom.getEmblemImageUrl() != null) {
                            s3Service.delete(custom.getEmblemImageUrl());
                        }
                    });
        }

        // 6. 주문 상태 변경 + 소프트 삭제
        order.updateStatus(OrderStatus.CANCELED);
        order.delete();
    }
}

