package brittaju.domain.service;

import brittaju.domain.model.GamePvP;
import brittaju.web.model.GameWebPvP;
import brittaju.web.model.LeaderboardEntry;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GameServicePvP {

    boolean isValidationBoard(GamePvP game);

    Integer isGameEnd(GamePvP game);

    GameWebPvP createNewGame(UUID userId);

    Optional<GameWebPvP> getById(UUID gameId);

    List<GameWebPvP> listAvailable();

    GameWebPvP joinGame(GameWebPvP incomingGame, UUID joiningUserId);

    GamePvP makeMove(GamePvP incomingGame, UUID currentUserId);

    List<GameWebPvP> getFinishedGamesByUser(UUID userId);

    List<LeaderboardEntry> getTopPlayers(int limit);

}
