package chess.chess_game.repository;

import chess.chess_game.entity.MatchRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRequestRepository extends JpaRepository<MatchRequest, Long> {
}
