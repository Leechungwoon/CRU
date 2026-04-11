package com.example.cru.domain.s3;

import com.example.cru.common.exception.CustomException;
import com.example.cru.common.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    public S3Service(S3Client s3Client, S3Presigner s3Presigner) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
    }

    //application.yml에서 버킷 이름 주입
    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * 이미지 업로드
     *
     * @param file   업로드할 이미지 파일
     * @param folder 저장할 폴더
     * @return 저장된 S3 키 (파일 경로)
     */
    public String upload(MultipartFile file, String folder) {

        // 이미지 파일 검증
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new CustomException(ErrorCode.INVALID_FILE_TYPE);
        }

        //UUID 고유한 파일 셍성 - 중복 방지
        String fileName = folder + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucket) // 버킷 이름
                    .key(fileName) // 파일 경로
                    .contentType(contentType) // 파일 타입(폴더/파일명)
                    .build(); // 파일 타입 (image/jpeg 등)

            s3Client.putObject(request, RequestBody.fromInputStream(
                    file.getInputStream(), // 파일 데이터 크기
                    file.getSize() // 파일 크기
            ));
        } catch (IOException e) {
            throw new CustomException(ErrorCode.S3_UPLOAD_FAILED);
        }
        return fileName; //저장된 키 반환 -> DB에 저장
    }

    /**
     * Presigned URL 생성
     * 버킷이 막혀있어도 임시로 이미지 접근 가능한 URL 발급
     * 유효시간 10분 이후 자동 만료
     *
     * @param key S3 저장된 파일 경로
     * @return
     */
    public String presignedUrl(String key) {

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10)) //유효시간 10분
                .build();

        return s3Presigner.presignGetObject(presignRequest)
                .url()
                .toString();// 임시 URL 반환
    }

    /**
     * 주문 취소 시 업로드된 이미지 삭제
     *
     * @param key S3에 저장된 파일 경로
     */
    public void delete(String key) {
        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        // S3에서 파일 삭제
        s3Client.deleteObject(request);
    }
}
