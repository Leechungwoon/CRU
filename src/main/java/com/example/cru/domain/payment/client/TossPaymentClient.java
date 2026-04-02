package com.example.cru.domain.payment.client;

import com.example.cru.common.Config.TossPaymentConfig;
import com.example.cru.domain.payment.model.request.TossPaymentRequest;
import com.example.cru.domain.payment.model.response.TossPaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TossPaymentClient {

    private final RestTemplate restTemplate;
    private final TossPaymentConfig tossPaymentConfig;

    //Toss 결제 승인
    public String TossPaySuccess(String paymentKey, Long orderId, int amount) {

        //인증 헤더 만들기
        HttpHeaders httpHeaders = buildHeaders();

        TossPaymentRequest body = new TossPaymentRequest(
                paymentKey,
                String.valueOf(orderId), // Toss는 orderId를 String으로 받음
                amount
        );

        HttpEntity<TossPaymentRequest> entity = new HttpEntity<>(body, httpHeaders);

        try {
            ResponseEntity<TossPaymentResponse> response = restTemplate.exchange(
                    TossPaymentConfig.CONFIRM_URL,
                    HttpMethod.POST,
                    entity, TossPaymentResponse.class
            );

            TossPaymentResponse result = response.getBody();

            //DONE 또는 승인 실패 - 예외 던져서 트랜잭션 롤백
            if (result == null || !"DONE".equals(result.getStatus())) {
                throw new RuntimeException("Toss 결제 승인 실패");
            }

            return result.getPaymentKey();

        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Toss API 오류: " + e.getResponseBodyAsString());
        }
    }

    //Toss 결제 취소
    //PaymentKey로 해당 결제를 특정하고 취소 이유를 담아 요청
    //카드사까지 취소가 전달됨

    public void tossCancel(String paymentKey, String cancelReason) {
        HttpHeaders httpHeaders = buildHeaders();

        Map<String, String> body = Map.of("cancelReason", cancelReason);// 전액 취소

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, httpHeaders);

        String url = String.format(TossPaymentConfig.CANCEL_URL, paymentKey); //URL에 paymentKey 삽입

        try {
            restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Toss 취소 API 오류: " + e.getResponseBodyAsString());
        }
    }


    //공통 인증 헤더 생성
    public HttpHeaders buildHeaders() {
        HttpHeaders httpHeaders = new org.springframework.http.HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", "Basic" + tossPaymentConfig.getEncodeSecretKey());
        return httpHeaders;
    }


}
