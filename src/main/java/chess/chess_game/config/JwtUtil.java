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

    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24시간

    // JWT 토큰 생성
    public String generateToken(String email) {
        System.out.println("토큰생성시 이메일 확인");
        System.out.println(email);
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, getSecretKey())
                .compact();
    }

    // JWT에서 사용자 이름 추출
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // JWT 유효성 검사
    public boolean validateToken(String token, String email) {
        String extractedUsername = extractUsername(token);
        return (email.equals(extractedUsername) && !isTokenExpired(token));
    }

    // JWT 만료 여부 확인
    private boolean isTokenExpired(String token) {
        Date expirationDate = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expirationDate.before(new Date());
    }
}
