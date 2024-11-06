package chess.chess_game.service;

import chess.chess_game.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {


    public String register(RegisterRequest request){
        System.out.println("@@@@@@@@@@@@@service@@@@@@@@@");
        System.out.println("test");
        return "";
    }
}
