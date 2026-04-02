package com.example.cru.common.exception;

import com.example.cru.common.model.CommonResponse;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GlobalExceptionHandler {

    /**
     * 낙관적 락 충돌 처리
     * 동시에 같은 주문에 결제 요청이 들어왔을 때 발생
     * 먼저 처리된 요청이 @Version을 올림 -> 나중 요청은 version 불일치 -> 예외 발생
     * <p>
     * 예외 발생 시점이 트랜잭션이 이미 폴백 마킹된 상태라 롤백 마킹된 트랜잭션은 커밋이 안됨
     * 그래서 Service 안에서 잡는 것이 아닌 밖에서 잡아야함
     *
     * @param e OptimisticLockingFailureException - Version 충돌 시 JPA가 자동으로 던짐
     * @return 409 CONFLICT - 동시 요청 충돌 의미함
     */
    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<CommonResponse> handleOptimisticLock(OptimisticLockingFailureException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) //409 동시 요청 충돌
                .body(CommonResponse.error("동시에 요청 처리되었습니다. 다시 시도해주세요."));
    }

    /**
     * 일반 예외 처리
     *
     * @param e RuntimeException - Service에서 예외
     * @return 400 BAD_REQUEST - 잘못된 요청
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CommonResponse> handleRuntimeException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) //400 에러
                .body(CommonResponse.error(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse> handleException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)// 500
                .body(CommonResponse.error("서버 오류가 발생했습니다.")); //내부는 상세 내용 노출 금지
    }
}
