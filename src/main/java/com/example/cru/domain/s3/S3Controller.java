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

        String key = s3Service.upload(file, folder);

        return ResponseEntity.ok(CommonResponse.success("이미지 업로드 완료", key));

    }
}
