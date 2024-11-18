package chess.chess_game.dto;

import chess.chess_game.entity.GameRoom;
import lombok.Data;

@Data
public class GameRoomDto {

    private Long id;
    private String gameMode;
    private String player1Username;
    private String player2Username;

    public GameRoomDto(GameRoom gameRoom) {
        this.id = gameRoom.getId();
        this.gameMode = gameRoom.getGameMode();
        this.player1Username = gameRoom.getUser1().getUsername();
        this.player2Username = gameRoom.getUser2().getUsername();
    }
}
