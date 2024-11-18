package chess.chess_game.dto;

import lombok.Data;

@Data
public class MatchRequestDto {
    private Long userId;
    private String gameMode; // "bullet", "blitz", "rapid"
}
