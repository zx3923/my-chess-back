package chess.chess_game.repository;

import chess.chess_game.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일을 기준으로 사용자 조회
    Optional<User> findByEmail(String email);

    // 이메일 중복 여부 체크
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}