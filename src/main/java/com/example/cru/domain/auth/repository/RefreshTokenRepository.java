package com.example.cru.domain.auth.repository;

import com.example.cru.domain.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    // Refresh Token 저장 로직에 사용
    Optional<RefreshToken> findUserId(Long userId);

    // DB 조회 여부 로직에 사용
    Optional<RefreshToken> findByToken(String refreshToken);

    // 로그아웃에서 사용
    void deleteByUserId(Long userId);
}
