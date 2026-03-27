package com.example.cru.common.filter;


import com.example.cru.common.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;


public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * 요청 시 JWT 인증 처리하는 메서드
     * @param request HTTP 요청 객체 (헤더,url 등)
     * @param response HTTP 응답 객체
     * @param filterChain 다음 필터로 넘기는 역할
     * @throws ServletException 필터 처리 중 서블릿 예외 발생 시
     * @throws IOException 입출력 예외 발생 시
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //헤더에서 값 꺼내기
        String bearer = request.getHeader("Authorization");

        //"Authorization 헤더가 존재하고 "Bearer" 로 시작하는 경우에만 처리
        if (bearer != null && bearer.startsWith("Bearer ")) {

            //"Bearer" 이후의 실제 토큰 값만 추출(앞 7글자 제거)
            String token = bearer.substring(7);

            // 토큰 유효성 검즘 (만료 여부, 서명 일치 여부 등)
            if (jwtUtil.validateToken(token)) {
                Long userId = jwtUtil.getUserId(token);

                //Spring Security 인증 객체 생성
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId, null, List.of()
                        );
                // SecurityContex에 인증 정보 저장 -> 이후 요청에서 인증된 사용자로 처리됨
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 인증 처리 후 다음 필터로 요청 넘기기
        filterChain.doFilter(request, response);
    }
}