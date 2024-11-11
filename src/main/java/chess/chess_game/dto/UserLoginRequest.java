package chess.chess_game.dto;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String email;
    private String password;
}
