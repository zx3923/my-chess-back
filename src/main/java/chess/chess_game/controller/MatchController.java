package chess.chess_game.controller;

import chess.chess_game.dto.MatchResult;
import chess.chess_game.entity.User;
import chess.chess_game.service.MatchService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MatchController {

    private final MatchService matchService;
    private final SimpMessagingTemplate messagingTemplate;

    public MatchController(MatchService matchService, SimpMessagingTemplate messagingTemplate) {
        this.matchService = matchService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/match")
    public void matchUser(User user) {
        MatchResult result = matchService.processMatchRequest(user);

        if (result.isSuccess()) {
            // 매칭 성공 시 결과를 WebSocket으로 전송
            messagingTemplate.convertAndSend("/topic/match-success", result);
        }
    }
}