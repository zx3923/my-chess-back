package chess.chess_game.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "[user]")
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
}