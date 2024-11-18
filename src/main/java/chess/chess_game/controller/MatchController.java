package chess.chess_game.controller;

import chess.chess_game.dto.GameRoomDto;
import chess.chess_game.dto.MatchRequestDto;
import chess.chess_game.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/match")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @PostMapping("/request")
    public ResponseEntity<String> requestMatch(@RequestBody MatchRequestDto requestDto) {

        boolean isQueued = matchService.addMatchRequest(requestDto);

        if (isQueued) {
            return ResponseEntity.ok("매칭 돌리기 성공");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("요청 처리 불가");
        }
    }

    @GetMapping("/status/{userId}")
    public ResponseEntity<GameRoomDto> checkMatchStatus(@PathVariable Long userId) {
        GameRoomDto gameRoom = matchService.getGameRoomForUser(userId);
        if (gameRoom != null) {
            return ResponseEntity.ok(gameRoom);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
