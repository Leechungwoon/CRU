package com.example.cru.domain.user.controller;

import com.example.cru.common.model.CommonResponse;
import com.example.cru.domain.user.model.RegisterUserRequest;
import com.example.cru.domain.user.model.CreateUserResponse;
import com.example.cru.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    //회원 생성
    @PostMapping("/users")
    public ResponseEntity<CommonResponse> createUserApi(@RequestBody RegisterUserRequest request) {

        //비지니스 로직
        CreateUserResponse response = userService.registerUser(request);

        //Dto 반환
        return ResponseEntity.ok(CommonResponse.success("회원을 생성했습니다.", response));
    }
}
