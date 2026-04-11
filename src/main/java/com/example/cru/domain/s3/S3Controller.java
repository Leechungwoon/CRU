package com.example.cru.domain.s3;

import com.example.cru.common.model.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    //이미지 업로드
    @PostMapping("/upload")
    public ResponseEntity<CommonResponse> upload(@RequestPart("file") MultipartFile file, @RequestParam(defaultValue = "images") String folder) {

        //비지니스 로직
        String key = s3Service.upload(file, folder);

        //반환
        return ResponseEntity.ok(CommonResponse.success("이미지 업로드 완료", key));
    }

    @GetMapping("/presigend_Url")
    public ResponseEntity<CommonResponse> getPresignedUrl(@RequestParam String key) {

        //비지니스 로직
        String url = s3Service.presignedUrl(key);

        //반환
        return ResponseEntity.ok(CommonResponse.success("발급 완료됐습니다.", url));
    }
}
