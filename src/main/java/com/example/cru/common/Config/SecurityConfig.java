package com.example.cru.common.Config;


import com.example.cru.common.filter.JwtFilter;
import com.example.cru.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor

/**
 * Spring Security 설정 클래스 및 정보
 * 1. CSRF 비활성화 -> REST API는 CSRF 공격 위험이 없어서 꺼짐
 * 2. 세션 STAELESS -> JWT 방식이라 서버 세션 생성 안함
 * 3. URL 마다 인증 설정 -> 적힌 도메인 제외하고는 토큰 필요
 * 4.JWTFilter 등록 -> 모든 요청에서 토큰 검증
 * 5. PasswordEncoder -> 비밀번호 BCypt 암호화
 */
public class SecurityConfig {
    private final JwtUtil jwtUtil;

    /**
     * Spring Security 필터 체인 설정
     *
     * @param http HttpSecurity - Security 설정을 도와주는 빌더 객체
     * @return 설정된 필터 체인 반환
     * @throws Exception Security 설정에서 발생할 수 있는 에외
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) //Jwt는 session 세션 사용 안해서 CSRF 불필요
                .sessionManagement(session
                        -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //서버가 세션 안 만들게 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/login",
                                "/auth/logout",
                                "/auth/reissue",
                                "/api/users",
                                "api/images/**"

                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        new JwtFilter(jwtUtil), //JwtFilter 등록
                        UsernamePasswordAuthenticationFilter.class // 기본 필터 앞에 실행
                );
        return http.build();
    }

    /**
     * 비밀 번호 암호화
     *
     * @return 비밀번호 BCrypt 암호화 -> 단방향이라 복호화 불가능(해킹 시 안전)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 BCrypt 암호화
    }


}

