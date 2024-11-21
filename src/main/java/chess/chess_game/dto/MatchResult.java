package chess.chess_game.dto;

import lombok.Data;

@Data
public class MatchResult {
    private final boolean success;
    private final String player1;
    private final String player2;

    public MatchResult(boolean success, String player1, String player2) {
        this.success = success;
        this.player1 = player1;
        this.player2 = player2;
    }
}
