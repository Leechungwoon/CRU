package com.example.cru.domain.auth.service;

import com.example.cru.common.exception.CustomException;
import com.example.cru.common.exception.ErrorCode;
import com.example.cru.common.util.JwtUtil;
import com.example.cru.domain.auth.controller.AuthController;
import com.example.cru.domain.auth.entity.RefreshToken;
import com.example.cru.domain.auth.model.LoginRequest;
import com.example.cru.domain.auth.model.LoginResponse;
import com.example.cru.domain.auth.repository.RefreshTokenRepository;
import com.example.cru.domain.user.entity.User;
import com.example.cru.domain.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    public AuthService(JwtUtil jwtUtil, UserRepository userRepository, PasswordEncoder passwordEncoder, RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * 로그임 -> 토큰 생성
     *
     * @param request 이메일, 비밀번호
     * @return userId, JWT 토큰
     */
    //로그인 -> 토큰 생성
    public LoginResponse login(LoginRequest request) {

        // 회원 조회 - 이메일
        User founduser = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 비밀번호 데이터 불러오기
        String password = request.getPassword();
        String encodePassword = founduser.getPassword();
        boolean matches = passwordEncoder.matches(password, encodePassword);

        // 비밀번호 검증
        if (!passwordEncoder.matches(password, encodePassword)) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        //토큰 생성 60분
        String accessToken = jwtUtil.generateToken(
                founduser.getId(),
                founduser.getEmail(),
                founduser.getName(),
                founduser.getRole()
        );

        // Refresh Token 생성 + DB 저장 7일
        String refreshToken = saveRefreshToken(founduser.getId());

        return new LoginResponse(founduser.getId(), accessToken, refreshToken);
    }

    //Refresh Token 저장
    // 한 유저당 하나의 Refresh Token 유지
    // 이미 있으면 덮어쓰고 없르면 새로 생성

    /**
     * Refresh Token 저장
     * 한 유저당 하나의 Refresh Token 유지
     * 이미 있으면 덮어쓰고 없르면 새로 생성
     *
     * @param userId 유저 Id
     * @return 생성된 Refresh Token 문자열
     */
    @Transactional
    public String saveRefreshToken(Long userId) {

        String newToken = jwtUtil.generateRefreshToken(userId); //새로운 Refresh Token 생성
        LocalDateTime expiresAt = jwtUtil.getRefreshTokenExpiry(); // 만료 시각 (7일 후)

        refreshTokenRepository.findUserId(userId)
                .ifPresentOrElse(
                        existing -> existing.updateToken(newToken, expiresAt), // 기존 토큰 갱신
                        () -> refreshTokenRepository.save( // 없는 경우 새로 저장
                                RefreshToken.builder()
                                        .userId(userId)
                                        .token(newToken)
                                        .expiresAt(expiresAt)
                                        .build()
                        )
                );
        return newToken;
    }

    //Refresh Token 재발급
    // RefreshToken이 유용한 경우 새로운 Access Token 발급
    // 2중 검증: JWT 서명 겁증 + DB 존재 여부 확인

    /**
     * Refresh Token 재발급
     * RefreshToken이 유용한 경우 새로운 Access Token 발급
     * 2중 검증: JWT 서명 겁증 + DB 존재 여부 확인
     *
     * @param refreshToken 클라이언트가 보낸 Refresh Token
     * @return 새로 발급된 Refresh Token
     */
    @Transactional
    public String reissueAccessToken(String refreshToken) {

        // JWT 서명 검증 - 변조 여부 확인
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        // DB 조회 -> 로그아웃된 토큰 확인(DB에 없으면 로그아웃)
        RefreshToken saved = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_TOKEN));

        // DB 기준 만료 시각 2차 검증, JWT 검증과 별개로 DB 만료 시각도 확인
        if (!saved.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(saved); // 만료된 토큰 DB에서 삭제
            throw new CustomException(ErrorCode.EXPIRED_TOKEN);
        }

        // 유저 조회 후 새 Access Token 발급
        User user = userRepository.findById(saved.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 반환
        return jwtUtil.generateToken(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole()
        );
    }

    /**
     * 로그아웃
     *
     * @param userId 로그아웃할 유저 Id
     */
    @Transactional
    public void logout(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}
