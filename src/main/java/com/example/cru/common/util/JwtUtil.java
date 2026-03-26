package com.example.cru.common.util;

import com.example.cru.common.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String HEADER_KEY = "Authorization"; //클라이언트 요청 시 Authorization: Bearer {token} 형태로 보냄
    private static final String BEARER_PREFIX = "Bearer"; //JWT 앞에 붙는 타입
    private static final long TOKEN_TIME = 60 * 60 * 1000L; // 토큰 유효 시간(60분)

    //JWT 성명에 사용될 비밀 키
    @Value("${JWT_SECRET_KEY}")
    private String secreKeyString;

    private SecretKey key; // JWT 서명/검증에 사용할 암호화 키 객체
    private JwtParser parser; //JWT 토큰을 파싱하고 검증하는 재사용되는 파서


    @PostConstruct
    public void init() {
        byte[] bytes = Decoders.BASE64.decode(secreKeyString); // secret key -> byte[]로 변환
        this.key = Keys.hmacShaKeyFor(bytes); //byte 배열을 HMAC-SHA 알고리즘용 Key 객체로 변환
        this.parser = Jwts.parser() // 토큰 파싱 및 검증할 parser미리 생성 (재사용 목적)
                .verifyWith(this.key)
                .build();
    }

    /**
     * 토큰 생성
     *
     * @param userId 유저 식별자
     * @param email  유저 이메일
     * @param name   유저 이름
     * @param role   유저 권한
     * @return "Bearer {JWT토큰} 형태의 문자열"
     */
    //토큰생성
    public String generateToken(Long userId, String email, String name, UserRole role) {
        Date now = new Date();

        return BEARER_PREFIX + Jwts.builder()
                .subject(userId.toString()) //토큰 주체
                .claim("email", email) //추가정보: 이메일
                .claim("name", name)
                .claim("role", role.name())
                .issuedAt(now)//발급 시각
                .expiration(new Date(now.getTime() + TOKEN_TIME)) //만료 시각
                .signWith(key, Jwts.SIG.HS256) //암호화 알고리즘
                .compact(); //최종 문자열 반환
    }

    /**
     * Authorization 헤더 유효성 체크
     *
     * @param header Authorization 헤더값
     * @return Bearer로 시작하면 true, 아니면 false
     */
    //bearer 체크
    public boolean hasAuthorizationHeader(String header) {
        return header != null && header.startsWith(BEARER_PREFIX);
    }

    /**
     * 토큰 헤더 분리
     *
     * @param tokenValue "Bearer {token}" 형태의 문자열
     * @return JWT 토큰 문자열
     */
    //토큰 헤더 분리
    public String substringToken(String tokenValue) {
        if (hasAuthorizationHeader(tokenValue)) {
            return tokenValue.substring(7);
        }
//        throw new CustomException(ExceptionCode.NOT_FOUNF_TOKEN);
        throw new RuntimeException("유호한 토큰이 아닙니다");
    }

    /**
     * 토큰 유효성 검증
     *
     * @param token JWT 토큰
     * @return 유효:true/ 아닌경우:false
     */
    //토큰 검증
    public boolean validateToken(String token) {
        if (token == null || token.isEmpty())
            return false;

        try {
            parser.parseSignedClaims(token);// 서명검증
            return true;
        } catch (Exception e) {
            return false; //만료 또는 아닌경우 false
        }
    }

    /**
     * 토큰 복호화(Claims 추출)
     *
     * @param token JWT 토큰
     * @return 토큰 안의 Claims 정보
     */
    //토큰 복호화
    public Claims extractAllClaims(String token) {
        return parser.parseSignedClaims(token).getPayload();
    }

    /**
     * 토큰 남은 유호 시간 확인
     *
     * @param token JWT 토큰
     * @return 남은 유효 시간(ms), 만료 시 0
     */
    //토큰 남은 유효시간 체크
    public long getRemainingTime(String token) {

        // 토큰에서 만료 시간 꺼냄
        Date expiration = extractAllClaims(token).getExpiration();

        //현 시간
        long now = new Date().getTime();

        //만료 시간 - 현재 시간 = 남은 시간 (음수는 0 반환)
        return Math.max(expiration.getTime() - now, 0);
    }
}
