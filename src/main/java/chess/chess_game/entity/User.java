package chess.chess_game.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "[USER]")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String username;
    private String email;
    private String passwordHash;

    // 게임 모드별 레이팅
    private int bulletRating;
    private int blitzRating;
    private int rapidRating;

    private LocalDateTime createdAt;

    // 선택한 게임 모드와 매칭 상태
    private String selectedGameType;
    private boolean isMatched;

    // 게임 모드에 따른 레이팅 반환
    public int getRatingByMode(String gameMode) {
        switch (gameMode.toLowerCase()) {
            case "bullet":
                return bulletRating;
            case "blitz":
                return blitzRating;
            case "rapid":
                return rapidRating;
            default:
                throw new IllegalArgumentException("잘못된 모드: " + gameMode);
        }
    }
}