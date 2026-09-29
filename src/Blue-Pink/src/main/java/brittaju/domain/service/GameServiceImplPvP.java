package brittaju.domain.service;

import brittaju.datasource.mapper.GameEntityMapperPvP;
import brittaju.datasource.model.GameEntityPvP;
import brittaju.datasource.repository.GameRepositoryPvP;
import brittaju.domain.model.*;
import brittaju.web.mapper.GameWebMapperPvP;
import brittaju.web.model.GameWebPvP;
import brittaju.web.model.LeaderboardEntry;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GameServiceImplPvP implements GameServicePvP {

    private final GameRepositoryPvP repository;
    private static final Logger log = LoggerFactory.getLogger(GameServiceImplPvP.class);

    public GameServiceImplPvP(GameRepositoryPvP repository) {
        this.repository = repository;
    }

    @Override
    public boolean isValidationBoard(GamePvP game) {
        Optional<GameEntityPvP> optionalEntity = repository.findById(game.getId());
        if (optionalEntity.isEmpty()) {
            return false;
        }
        GameEntityPvP oldGameEntity = optionalEntity.get();
        GamePvP oldGame = GameEntityMapperPvP.toDomain(oldGameEntity);
        if (!oldGame.getId().equals(game.getId())) {
            return false;
        }
        BoardPvP oldBoard = oldGame.getBoard();
        BoardPvP newBoard = game.getBoard();
        int count = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (oldBoard.getCell(i, j) != newBoard.getCell(i, j)) {
                    if (oldBoard.getCell(i, j) != 0) {
                        return false;
                    }
                    count++;
                    if (count > 1) {
                        return false;
                    }
                }
            }
        }
        return count != 0;
    }

    @Override
    public Integer isGameEnd(GamePvP game) {
        if (checkWinner(game.getBoard()) == -1) {
            return -1;
        } else if (checkWinner(game.getBoard()) == 1) {
            return 1;
        } else if (isBoardFull(game.getBoard())) {
            return 0;
        } else {
            return null;
        }
    }

    private int checkWinner(BoardPvP board) {
        for (int i = 0; i < 3; i++) {
            if (board.getCell(i, 0) != 0 && board.getCell(i, 0) == board.getCell(i, 1) && board.getCell(i, 0) == board.getCell(i, 2)) {
                return board.getCell(i, 0);
            }
            if (board.getCell(0, i) != 0 && board.getCell(0, i) == board.getCell(1, i) && board.getCell(0, i) == board.getCell(2, i)) {
                return board.getCell(0, i);
            }
        }
        if (board.getCell(0, 0) != 0 && board.getCell(0, 0) == board.getCell(1, 1) && board.getCell(0, 0) == board.getCell(2, 2)) {
            return board.getCell(0, 0);
        }
        if (board.getCell(0, 2) != 0 && board.getCell(0, 2) == board.getCell(1, 1) && board.getCell(0, 2) == board.getCell(2, 0)) {
            return board.getCell(0, 2);
        }
        return 0;
    }

    private boolean isBoardFull(BoardPvP board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board.getCell(i, j) == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    @Transactional
    public GameWebPvP createNewGame(UUID userId) {
        List<GameStatus> active = List.of(
                GameStatus.WAITING_FOR_PLAYERS,
                GameStatus.PLAYER_X_TURN,
                GameStatus.PLAYER_O_TURN
        );

        List<GameEntityPvP> existing = repository.findActiveByUser(userId, active);
        if (!existing.isEmpty()) {
            return GameWebMapperPvP.fromDomain(
                    GameEntityMapperPvP.toDomain(existing.getFirst())
            );
        }

        BoardPvP newBoard = new BoardPvP();
        GamePvP newGame = new GamePvP(newBoard);

        boolean creatorIsX = Math.random() < 0.5;
        if (creatorIsX) {
            newGame.setPlayerXid(userId);
        } else {
            newGame.setPlayerOid(userId);
        }

        repository.save(GameEntityMapperPvP.fromDomain(newGame));
        log.info("Новая игра успешно сохранена");
        return GameWebMapperPvP.fromDomain(newGame);
    }

    @Override
    public Optional<GameWebPvP> getById(UUID gameId) {
        return repository.findById(gameId)
                .map(GameEntityMapperPvP::toDomain)
                .map(GameWebMapperPvP::fromDomain);
    }

    @Override
    public List<GameWebPvP> listAvailable() {
        return repository.findByStatus(GameStatus.WAITING_FOR_PLAYERS)
                .stream()
                .map(GameEntityMapperPvP::toDomain)
                .map(GameWebMapperPvP::fromDomain)
                .toList();
    }

    @Override
    @Transactional
    public GameWebPvP joinGame(GameWebPvP incomingGame, UUID joiningUserId) {
        UUID gameId = incomingGame.id();
        if (gameId == null) {
            throw new IllegalArgumentException("Игра не найдена");
        }
        GameEntityPvP entity = repository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Игра не найдена"));
        GamePvP game = GameEntityMapperPvP.toDomain(entity);
        if (game.getStatus() != GameStatus.WAITING_FOR_PLAYERS) {
            throw new IllegalStateException("Игра уже началась или завершена");
        }
        if (joiningUserId.equals(game.getPlayerXid())
                || joiningUserId.equals(game.getPlayerOid())) {
            throw new IllegalStateException("Вы уже в этой игре");
        }
        if (game.getPlayerXid() == null) {
            game.setPlayerXid(joiningUserId);
        } else if (game.getPlayerOid() == null) {
            game.setPlayerOid(joiningUserId);
        } else {
            throw new IllegalStateException("Места заняты");
        }
        if (game.getPlayerXid() != null && game.getPlayerOid() != null) {
            game.setStatus(GameStatus.PLAYER_X_TURN);
        }
        repository.save(GameEntityMapperPvP.fromDomain(game));
        log.info("Игрок {} присоединился к игре {}. X={}, O={}, status={}",
                joiningUserId, gameId, game.getPlayerXid(), game.getPlayerOid(), game.getStatus());
        return GameWebMapperPvP.fromDomain(game);
    }

    @Override
    @Transactional
    public GamePvP makeMove(GamePvP incomingGame, UUID currentUserId) {
        GameEntityPvP entity = repository.findById(incomingGame.getId())
                .orElseThrow(() -> new IllegalArgumentException("Игра не найдена"));
        GamePvP game = GameEntityMapperPvP.toDomain(entity);
        boolean isX = currentUserId.equals(game.getPlayerXid());
        boolean isO = currentUserId.equals(game.getPlayerOid());
        if (game.getStatus() == GameStatus.PLAYER_X_TURN && !isX) {
            throw new IllegalStateException("Сейчас не ваш ход");
        }
        if (game.getStatus() == GameStatus.PLAYER_O_TURN && !isO) {
            throw new IllegalStateException("Сейчас не ваш ход");
        }
        if (game.getStatus() == GameStatus.PLAYER_X_WON
                || game.getStatus() == GameStatus.PLAYER_O_WON
                || game.getStatus() == GameStatus.DRAW) {
            throw new IllegalStateException("Игра уже завершена");
        }
        if (game.getStatus() == GameStatus.WAITING_FOR_PLAYERS) {
            throw new IllegalStateException("Игра ещё не началась");
        }
        GamePvP gameWithNewBoard = new GamePvP(
                game.getId(),
                incomingGame.getBoard(),
                game.getPlayerXid(),
                game.getPlayerOid(),
                game.getStatus(),
                game.getCreatedAt()
        );
        Integer endGame = isGameEnd(gameWithNewBoard);
        if (endGame != null) {
            switch (endGame) {
                case 0  -> gameWithNewBoard.setStatus(GameStatus.DRAW);
                case 1  -> gameWithNewBoard.setStatus(GameStatus.PLAYER_X_WON);
                case -1 -> gameWithNewBoard.setStatus(GameStatus.PLAYER_O_WON);
            }
        } else {
            if (gameWithNewBoard.getStatus() == GameStatus.PLAYER_X_TURN) {
                gameWithNewBoard.setStatus(GameStatus.PLAYER_O_TURN);
            } else if (gameWithNewBoard.getStatus() == GameStatus.PLAYER_O_TURN) {
                gameWithNewBoard.setStatus(GameStatus.PLAYER_X_TURN);
            }
        }
        GameEntityPvP toSave = GameEntityMapperPvP.fromDomain(gameWithNewBoard);
        repository.save(toSave);
        log.info("After move: x={}, o={}, status={}",
                gameWithNewBoard.getPlayerXid(),
                gameWithNewBoard.getPlayerOid(),
                gameWithNewBoard.getStatus());
        return gameWithNewBoard;
    }

    @Override
    public List<GameWebPvP> getFinishedGamesByUser(UUID userId) {
        return repository.findFinishedByUser(userId).stream()
                .map(GameEntityMapperPvP::toDomain)
                .map(GameWebMapperPvP::fromDomain)
                .toList();
    }

    @Override
    public List<LeaderboardEntry> getTopPlayers(int limit) {
        if (limit <= 0) {
            limit = 10;
        }
        if (limit > 100) {
            limit = 100;
        }

        return repository.findTopPlayers(limit).stream()
                .map(p -> new LeaderboardEntry(
                        p.getUserId(),
                        p.getLogin(),
                        p.getWins() == null ? 0 : p.getWins(),
                        p.getDraws() == null ? 0 : p.getDraws(),
                        p.getLosses() == null ? 0 : p.getLosses(),
                        p.getWinRate() == null ? 0.0 : p.getWinRate()
                ))
                .toList();
    }

}