package chess.chess_game.dto;

import lombok.Data;

@Data
public class MatchRequest {
    private Long userId;
    private String gameMode; // bullet, blitz, or rapid
    private int rating;
}