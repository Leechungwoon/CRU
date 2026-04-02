package com.example.cru.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //ITEM
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND,"아이템을 찾을 수 없습니다."),

    //USER
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"사용자를 찾을 수 없습니다." ),

    //ORDER
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."), //404
    ORDER_NOT_PAYABLE(HttpStatus.BAD_REQUEST, "결제할 수 없는 주문입니다."), //400
    ORDER_NOT_CANCELABLE(HttpStatus.BAD_REQUEST, "취소할 수 없는 주문입니다."), //400


    //Payment
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "결제 정보를 찾을 수 없습니다."), //404
    PAYMENT_ALREADY_CANCELED(HttpStatus.BAD_REQUEST, "이미 취소된 결제입니다."), //400
    INVALID_AMOUNT(HttpStatus.BAD_REQUEST, "결제 금액이 주문 금액과 다릅니다."), //400


    // 공통,
    UNAUTHORIZED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    TOSS_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "결제 처리 중 오류가 발생했습니다.");


    private final HttpStatus status; //HTTP 상태 코드
    private final String message; //클라이언트에게 반환할 메시지

}
