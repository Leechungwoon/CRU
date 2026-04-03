package com.example.cru.domain.auth.controller;

import com.example.cru.common.model.CommonResponse;
import com.example.cru.common.util.JwtUtil;
import com.example.cru.domain.auth.model.LoginRequest;
import com.example.cru.domain.auth.model.LoginResponse;
import com.example.cru.domain.auth.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    //Access Token 재발급

    /**
     * Access Token 재발급
     *
     * @param refreshToken 클라이언트가  보낸 Refresh Token
     * @return 새로 발급된 Access Token
     */
    @PostMapping("/reissue")
    public ResponseEntity<CommonResponse> reissue(
            @RequestHeader("Refresh-Token") String refreshToken
    ) {
        String newAccessToken = authService.reissueAccessToken(refreshToken);

        return ResponseEntity.ok(CommonResponse.success("Access Token이 제발급됐습니다.", newAccessToken));
    }

    /**
     * 로그아웃
     *
     * @return 200반환 -> 토큰 삭제
     */
    @DeleteMapping("/logout")
    public ResponseEntity<CommonResponse> logout(@AuthenticationPrincipal Long userId) {

        authService.logout(userId);

        return ResponseEntity.ok(CommonResponse.success("로그아웃 됐습니다.", null));
    }
}
