package com.example.cru.common.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(); //RuntimeException에 메시지 전달
        this.errorCode = errorCode;
    }
}
