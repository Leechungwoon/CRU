package com.example.cru.common.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {

    //AWS_ACCESS_ID -> 환경 변수에 주입
    @Value("${AWS_ACCESS_KEY}")
    private String accessKey;

    //AWS_SECRET_KEY -> 환경 변수에 주입
    @Value("${AWS_SECRET_KEY}")
    private String secretKey;

    /**
     * S3 클라이언트 빈 등록
     * 파일 업로드 및 삭제 시 사용 예정
     *
     * @return
     */
    @Bean
    public S3Client s3Client() {
        // 키를 이용하여 자격증명 생성
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        return S3Client.builder()
                .region(Region.AP_NORTHEAST_2)// 서울 리전
                .credentialsProvider(StaticCredentialsProvider.create(credentials)) // 자격증명 주입
                .build();
    }

    /**
     * S3 Presigner 빈 등록
     * Presigend URL 생성 시 사용
     * Presigend URL = 일정 시간 동안만 유효한 임시 접근 URL
     * 버킷을 퍼블릭으로 열지 않아도 이미지 접근 가능
     *
     * @return
     */
    @Bean
    public S3Presigner s3Presigner() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        return S3Presigner.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }
}
