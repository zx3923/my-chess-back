package chess.chess_game.repository;

import chess.chess_game.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일 중복 체크
    boolean existsByEmail(String email);

    // 유저명 중복 체크
    boolean existsByUsername(String username);

    // 이메일로 사용자 조회
    User findByEmail(String email);
}