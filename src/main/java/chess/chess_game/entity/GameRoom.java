package chess.chess_game.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class GameRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user1;

    @ManyToOne
    private User user2;

    private String gameMode; // "bullet", "blitz", "rapid" 중 하나
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status; // "WAITING", "IN_PROGRESS", "COMPLETED" 등
}
