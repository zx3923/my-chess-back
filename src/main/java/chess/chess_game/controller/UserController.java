package chess.chess_game.controller;

import chess.chess_game.config.JwtUtil;
import chess.chess_game.dto.TokenRequest;
import chess.chess_game.dto.UserLoginRequest;
import chess.chess_game.dto.UserRegistrationRequest;
import chess.chess_game.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody UserRegistrationRequest request) {
        try {
            Map<String, Object> result = userService.registerUser(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody UserLoginRequest request) {
        try {
            Map<String, Object> result = userService.loginUser(request);
            String token = jwtUtil.generateToken(request.getEmail());
            String refreshToken = jwtUtil.generateRefreshToken(request.getEmail());
            result.put("token", token);
            result.put("refreshToken", refreshToken);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> testEndpoint(@RequestHeader("Authorization") String authorizationHeader) {
        Map<String, Object> result = new HashMap<>();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            try {
                String email = jwtUtil.extractUsername(jwtToken);
                result.put("status", true);
                result.put("message", "토큰이 유효합니다.");
                return ResponseEntity.ok(result);
            } catch (ExpiredJwtException e) {
                result.put("status", false);
                result.put("message", "만료된 토큰입니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
            } catch (Exception e) {
                result.put("status", false);
                result.put("message", "잘못된 요청입니다.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }
        }
        result.put("status", false);
        result.put("message", "Authorization 헤더가 없습니다.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, Object>> refreshAccessToken(@RequestBody TokenRequest tokenRequest) {
        System.out.println("리프레시 토큰 발급 확인");
        Map<String, Object> result = new HashMap<>();
        String refreshToken = tokenRequest.getRefreshToken();

        try {
            // 리프레시 토큰 유효성 검사
            String email = jwtUtil.extractUsername(refreshToken);
            if (jwtUtil.validateToken(refreshToken, email)) {
                System.out.println("성공");
                // 새로운 엑세스 토큰 발급
                String newAccessToken = jwtUtil.generateToken(email);
                System.out.println(newAccessToken);
                result.put("token", newAccessToken);
                System.out.println(result);
                return ResponseEntity.ok(result);
            } else {
                System.out.println("실패");
                result.put("message", "잘못된 토큰");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
            }
        } catch (Exception e) {
            System.out.println("실패2");
            result.put("message", "잘못된 토큰");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }
    }

}
