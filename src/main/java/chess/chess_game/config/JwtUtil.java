package chess.chess_game.config;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret.key}")
    private String secretKey;  // 인스턴스 변수로 주입받은 값

    private static String SECRET_KEY;

    @PostConstruct
    public void init() {
        SECRET_KEY = secretKey;
    }

    public static String getSecretKey() {
        return SECRET_KEY;
    }

//    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 1; // 1시간

    private static final long EXPIRATION_TIME = 1000 * 60 * 1; // 테스트용

    private static final long REFRESH_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7; // 7일

    // JWT 토큰 생성
    public String generateToken(String email) {
        System.out.println("토큰 생성시 이메일 확인: " + email);
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, getSecretKey())
                .compact();
    }

    // 리프레시 토큰 생성
    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, getSecretKey())
                .compact();
    }

    // JWT에서 사용자 이름 추출
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .setAllowedClockSkewSeconds(60) // 허용 시간 차이 60초 설정
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // JWT 유효성 검사
    public boolean validateToken(String token, String email) {
        try {
            String extractedUsername = extractUsername(token);  // JWT에서 사용자 이름을 추출
            if (email.equals(extractedUsername) && !isTokenExpired(token)) {
                return true;
            }
            throw new ExpiredJwtException(null, null, "JWT 토큰 만료됨");  // 만료된 경우 명시적으로 예외를 던짐
        } catch (JwtException e) {
            System.out.println(e.getMessage());
            return false; // 잘못된 토큰 처리
        }
    }

    // JWT 만료 여부 확인
    private boolean isTokenExpired(String token) {
        Date expirationDate = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .setAllowedClockSkewSeconds(60) // 허용 시간 차이 60초 설정
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expirationDate.before(new Date());
    }


}
