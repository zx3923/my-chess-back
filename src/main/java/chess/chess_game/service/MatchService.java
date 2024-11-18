package chess.chess_game.service;

import chess.chess_game.dto.GameRoomDto;
import chess.chess_game.dto.MatchRequestDto;
import chess.chess_game.entity.GameRoom;
import chess.chess_game.entity.MatchRequest;
import chess.chess_game.entity.User;
import chess.chess_game.repository.GameRoomRepository;
import chess.chess_game.repository.MatchRequestRepository;
import chess.chess_game.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Queue;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final UserRepository userRepository;
    private final MatchRequestRepository matchRequestRepository;
    private final GameRoomRepository gameRoomRepository;

    private final Queue<MatchRequest> waitingQueue = new LinkedList<>();

    @Transactional
    public boolean addMatchRequest(MatchRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("잘못된 유저"));

        if (user.isMatched()) {
            throw new IllegalArgumentException("이미 매칭중인 유저");
        }

        MatchRequest matchRequest = new MatchRequest();
        matchRequest.setUser(user);
        matchRequest.setGameMode(requestDto.getGameMode());
        matchRequest.setRequestTime(LocalDateTime.now());


        matchRequestRepository.save(matchRequest);
        waitingQueue.offer(matchRequest);

        processMatchQueue();

        return true;
    }

    @Transactional
    public void processMatchQueue() {
        while (waitingQueue.size() >= 2) {
            MatchRequest request1 = waitingQueue.poll();
            MatchRequest request2 = waitingQueue.poll();

            if (canBeMatched(request1, request2)) {
                GameRoom gameRoom = createGameRoom(request1.getUser(), request2.getUser(), request1.getGameMode());
                gameRoomRepository.save(gameRoom);
                notifyPlayers(gameRoom); // 매칭 성공 알림
            } else {
                waitingQueue.offer(request1);
                waitingQueue.offer(request2);
                break;
            }
        }
    }

    private boolean canBeMatched(MatchRequest req1, MatchRequest req2) {
        return req1.getGameMode().equals(req2.getGameMode()) &&
                Math.abs(req1.getUser().getRatingByMode(req1.getGameMode()) -
                        req2.getUser().getRatingByMode(req2.getGameMode())) <= 100;
    }

    private GameRoom createGameRoom(User user1, User user2, String gameMode) {
        GameRoom gameRoom = new GameRoom();
        gameRoom.setUser1(user1);
        gameRoom.setUser2(user2);
        gameRoom.setGameMode(gameMode);
        gameRoom.setStartTime(LocalDateTime.now());
        gameRoom.setStatus("IN_PROGRESS");

        user1.setMatched(true);
        user2.setMatched(true);

        return gameRoom;
    }

    public GameRoomDto getGameRoomForUser(Long userId) {
        GameRoom gameRoom = gameRoomRepository.findById(userId)
                .orElse(null);

        if (gameRoom == null) {
            return null;
        }

        return new GameRoomDto(gameRoom);
    }

    private void notifyPlayers(GameRoom gameRoom) {
        // 게임방 정보 전달 구현
        System.out.println(gameRoom.getId());
    }
}
