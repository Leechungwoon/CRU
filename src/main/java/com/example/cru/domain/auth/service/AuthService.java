package com.example.cru.domain.auth.service;

import com.example.cru.common.util.JwtUtil;
import com.example.cru.domain.auth.controller.AuthController;
import com.example.cru.domain.auth.model.LoginRequest;
import com.example.cru.domain.auth.model.LoginResponse;
import com.example.cru.domain.user.entity.User;
import com.example.cru.domain.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    public AuthService(JwtUtil jwtUtil, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 로그임 -> 토큰 생성
     * @param request 이메일, 비밀번호
     * @return userId, JWT 토큰
     */
    //로그인 -> 토큰 생성
    public LoginResponse login(LoginRequest request) {

        // 회원 조회 - 이메일
        User founduser = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일을 찾을 수 없습니다."));

        // 비밀번호 일치하는지
        String password = request.getPassword();
        String encodePassword = founduser.getPassword();
        boolean match = passwordEncoder.matches(password, encodePassword);

        // false인 경우에 예외처리
        if (!match) {
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        }

        //토큰 생성
        String token = jwtUtil.generateToken(founduser.getId(), founduser.getEmail(), founduser.getName(), founduser.getRole());
        return new LoginResponse(founduser.getId(), token);
    }
}
