package chess.chess_game.service;

import chess.chess_game.dto.MatchResult;
import chess.chess_game.entity.User;
import org.springframework.stereotype.Service;

@Service
public class MatchService {

    private final MatchQueue matchQueue;

    public MatchService(MatchQueue matchQueue) {
        this.matchQueue = matchQueue;
    }

    public synchronized MatchResult processMatchRequest(User user) {
        matchQueue.addToQueue(user);

        // 대기열에서 매칭 가능한 사용자 확인
        User otherUser = matchQueue.getFromQueue();
        System.out.println("상대방 " + otherUser.getUserId());
        System.out.println("나 " + user.getUserId());

        if (otherUser == null || otherUser.getUserId().equals(user.getUserId())) {
            System.out.println("대기열에 다른 플레이어가 없거나 자기 자신일 경우");
            // 대기열에 다른 플레이어가 없거나 자기 자신일 경우 대기
            matchQueue.addToQueue(user);
            return new MatchResult(false, null, null);
        }

        // 사용자가 선호하는 게임 모드
        String userMode = user.getSelectedGameType();
        String potentialMatchMode = otherUser.getSelectedGameType();

        if (userMode.equals(potentialMatchMode)) {
            System.out.println("게임모드 및 레이팅비교");
            int userRating = user.getRatingByMode(userMode);
            int matchRating = otherUser.getRatingByMode(potentialMatchMode);

            // 레이팅 비교
            if (Math.abs(userRating - matchRating) <= 100) { // 허용 범위: ±100
                // 매칭 성공
                user.setMatched(true);
                otherUser.setMatched(true);
                System.out.println("매칭성공");
                return new MatchResult(true, user.getUsername(), otherUser.getUsername());
            }
        }

        // 매칭 실패, 다시 대기열에 추가
        matchQueue.addToQueue(otherUser);
        return new MatchResult(false, null, null);


    }
}
