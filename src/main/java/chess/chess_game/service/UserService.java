package chess.chess_game.service;

import chess.chess_game.dto.UserRegistrationRequest;
import chess.chess_game.entity.User;
import chess.chess_game.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Map<String, Object> registerUser(UserRegistrationRequest request) {
        Map<String, Object> response = new HashMap<>();

        // 유저명 중복 확인
        if (userRepository.existsByUsername(request.getUsername())) {
            response.put("status", false);
            response.put("message", "이미 존재하는 이름 입니다.");
            return response;
        }

        // 이메일 중복 확인
        if (userRepository.existsByEmail(request.getEmail())) {
            response.put("status", false);
            response.put("message", "이미 존재하는 이메일 입니다.");
            return response;
        }

        // 패스워드 일치 확인
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            response.put("status", false);
            response.put("message", "패스워드가 일치하지 않습니다.");
            return response;
        }

        // 생성 및 저장
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setBulletRating(1000); // 초기점수
        user.setBlitzRating(1000);
        user.setRapidRating(1000);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        response.put("status", true);
        response.put("message", "회원가입 성공!");
        return response;
    }
}
