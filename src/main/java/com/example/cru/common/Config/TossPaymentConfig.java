package com.example.cru.common.Config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
public class TossPaymentConfig {

    // Toss API 호출에 필요한 설정값 보관소
    // URL 이랑 인증 키 모아둔 곳

    @Getter
    @Value(("$[toss.client-key]")) //프론트에 전달에 사용
    private String clientKey;

    @Value("${toss.secret-key}") // yml에서 주입
    private String secretKey;

    //결제 승인 요청 URL
    public static final String CONFIRM_URL = "http://api.tosspayments.com/v1/payments/confirm";

    //결제 취소 요청 URL
    public static final String CANCEL_URL = "http://api.tosspayments.com/v1/payments/%s/cancel";

    //Toss 인증 헤더값
    public String getEncodeSecretKey() {
        String keyWithColon = secretKey + ":"; //secretKey: 형태로 만들기
        return Base64.getEncoder().encodeToString(keyWithColon.getBytes(StandardCharsets.UTF_8)); //베이스 64 인코딩
    }
}
