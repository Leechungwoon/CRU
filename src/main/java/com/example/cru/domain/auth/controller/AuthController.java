package com.example.cru.domain.auth.controller;

import com.example.cru.common.model.CommonResponse;
import com.example.cru.common.util.JwtUtil;
import com.example.cru.domain.auth.model.LoginRequest;
import com.example.cru.domain.auth.model.LoginResponse;
import com.example.cru.domain.auth.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthService authService;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    public AuthController(JwtUtil jwtUtil, AuthService authService) {
        this.jwtUtil = jwtUtil;
        this.authService = authService;
    }

    /**
     * 로그인
     *
     * @param request 이메일, 비밀번호
     * @return 이메일, 비밀번호 반환
     */
    @PostMapping("/login")
    public ResponseEntity<CommonResponse> loginApi(@RequestBody LoginRequest request) {
        log.info("AuthController.loginApi()");

        //비지니스 로직
        LoginResponse response = authService.login(request);

        //Dto 반환
        return ResponseEntity.ok(CommonResponse.success("로그인에 성공했습니다.", response));
    }

    /**
     * 로그아웃
     *
     * @return 200반환 -> 토큰 삭제
     */
    @PostMapping("/logout")
    public ResponseEntity<CommonResponse> logout() {
        return ResponseEntity.ok(CommonResponse.success("로그아웃 됐습니다."));
    }
}
