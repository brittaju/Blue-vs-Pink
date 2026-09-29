package brittaju.domain.service;

import brittaju.datasource.mapper.GameEntityMapperPvE;
import brittaju.datasource.model.GameEntityPvE;
import brittaju.datasource.repository.GameRepositoryPvE;
import brittaju.domain.model.BoardPvE;
import brittaju.domain.model.GamePvE;
import brittaju.web.mapper.GameWebMapperPvE;
import brittaju.web.model.GameWebPvE;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameServiceImplPvE implements GameServicePvE {

    private static final int AI_WIN_SCORE = 10;
    private static final int HUMAN_WIN_SCORE = -10;
    private static final int DRAW_SCORE = 0;
    private final GameRepositoryPvE repository;
    private static final Logger log = LoggerFactory.getLogger(GameServiceImplPvE.class);

    public GameServiceImplPvE(GameRepositoryPvE repository) {
        this.repository = repository;
    }

    @Override
    public int[] getBest(GamePvE game) {
        BoardPvE currentBoard = game.board();
        int[] best = {-1, -1};
        int bestScore = Integer.MIN_VALUE;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (currentBoard.getCell(i, j) == 0) {
                    BoardPvE nextBoard = currentBoard.currentCopy(BoardPvE.AI, i, j);
                    int score = minimax(nextBoard, false, 0);
                    if (score > bestScore) {
                        bestScore = score;
                        best[0] = i;
                        best[1] = j;
                    }
                }
            }
        }
        return best;
    }

    public int minimax(BoardPvE board, boolean isMaximizing, int depth) {
        int winner = checkWinner(board);
        if (winner != 0) {
            return (winner == BoardPvE.AI) ? (AI_WIN_SCORE - depth) : (HUMAN_WIN_SCORE + depth);
        }
        if (isBoardFull(board)) {
            return DRAW_SCORE;
        }

        if (isMaximizing) {
            int maxScore = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board.getCell(i, j) == 0) {
                        int score = minimax(board.currentCopy(BoardPvE.AI, i, j), false, depth + 1);
                        maxScore = Math.max(maxScore, score);
                    }
                }
            }
            return maxScore;
        } else {
            int minScore = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board.getCell(i, j) == 0) {
                        int score = minimax(board.currentCopy(BoardPvE.HUMAN, i, j), true, depth + 1);
                        minScore = Math.min(minScore, score);
                    }
                }
            }
            return minScore;
        }
    }

    @Override
    public boolean isValidationBoard(GamePvE game) {
        Optional<GameEntityPvE> optionalEntity = repository.findById(game.id());
        if (optionalEntity.isEmpty()) {
            return false;
        }
        log.info("Игра успешно найдена");
        GameEntityPvE oldGameEntity = optionalEntity.get();
        GamePvE oldGame = GameEntityMapperPvE.toDomain(oldGameEntity);
        if (!oldGame.id().equals(game.id())) {
            return false;
        }
        BoardPvE oldBoard = oldGame.board();
        BoardPvE newBoard = game.board();
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
    public Integer isGameEnd(GamePvE game) {
        if (checkWinner(game.board()) == -1) {
            return -1;
        } else if (checkWinner(game.board()) == 1) {
            return 1;
        } else if (isBoardFull(game.board())) {
            return 0;
        } else {
            return null;
        }
    }

    private int checkWinner(BoardPvE board) {
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

    private boolean isBoardFull(BoardPvE board) {
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
    public GameWebPvE createNewGame() {
        BoardPvE newBoard = new BoardPvE();
        GamePvE newGame = new GamePvE(newBoard);
        if (newGame.isAIGoesFirst()) {
            BoardPvE aiBoard = newBoard.currentCopy(-1, 1, 1);
            GamePvE gameWithAiMove = new GamePvE(newGame.id(), aiBoard, true);
            repository.save(GameEntityMapperPvE.fromDomain(gameWithAiMove));
            log.info("Новая игра успешно сохранена с учетом хода ИИ");
            return GameWebMapperPvE.fromDomainAI(gameWithAiMove);
        }
        repository.save(GameEntityMapperPvE.fromDomain(newGame));
        log.info("Новая игра успешно сохранена");
        return GameWebMapperPvE.fromDomainHuman(newGame);
    }

    @Override
    public GamePvE mapHumanMove(GameWebPvE incomingGame) {
        if (incomingGame.isAIGoesFirst()) {
            return GameWebMapperPvE.toDomainAI(incomingGame);
        } else {
            return GameWebMapperPvE.toDomainHuman(incomingGame);
        }
    }

    @Override
    @Transactional
    public GamePvE makeAIMove(GamePvE gameAfterHumanMove) {
        boolean isAIGoesFirst = gameAfterHumanMove.isAIGoesFirst();
        int[] bestMove = getBest(gameAfterHumanMove);
        BoardPvE boardWithAiMove = gameAfterHumanMove.board().currentCopy(-1, bestMove[0], bestMove[1]);
        GamePvE gameWithAiMove = new GamePvE(gameAfterHumanMove.id(), boardWithAiMove, isAIGoesFirst);
        repository.save(GameEntityMapperPvE.fromDomain(gameWithAiMove));
        log.info("Игра успешно сохранена");
        return gameWithAiMove;
    }

}