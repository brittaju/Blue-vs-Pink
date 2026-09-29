package brittaju.web.controller;

import brittaju.domain.model.GamePvE;
import brittaju.domain.service.GameServicePvE;
import brittaju.web.mapper.GameWebMapperPvE;
import brittaju.web.model.GameWebPvE;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/game/pve")
public class GameControllerPvE {

    private final GameServicePvE service;

    public GameControllerPvE(GameServicePvE service) {
        this.service = service;
    }

    @PostMapping("/")
    public ResponseEntity<GameWebPvE> postCreateNewGame() {
        GameWebPvE newGame = service.createNewGame();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newGame);
    }

    @PostMapping("/move")
    public ResponseEntity<?> postMakeMove(@RequestBody GameWebPvE incomingGame) {
        GamePvE gameAfterHumanMove = service.mapHumanMove(incomingGame);

        if (!service.isValidationBoard(gameAfterHumanMove)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Жульничать нехорошо:(");
        }

        Integer endGame = service.isGameEnd(gameAfterHumanMove);
        if (endGame != null) {
            if (endGame == 1) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(Map.of("message", "Ты сделал невозможное!!!", "game", incomingGame));
            } else if (endGame == -1) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(Map.of("message", "Тебе нужно больше тренироваться;)", "game", incomingGame));
            } else {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(Map.of("message", "А ты хорош:)", "game", incomingGame));
            }
        }

        GamePvE gameWithAiMove = service.makeAIMove(gameAfterHumanMove);

        GameWebPvE gameWebWithAiMove;
        if (gameWithAiMove.isAIGoesFirst()) {
            gameWebWithAiMove = GameWebMapperPvE.fromDomainAI(gameWithAiMove);
        } else {
            gameWebWithAiMove = GameWebMapperPvE.fromDomainHuman(gameWithAiMove);
        }

        endGame = service.isGameEnd(gameWithAiMove);
        if (endGame != null) {
            if (endGame == 1) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(Map.of("message", "Ты сделал невозможное!!!", "game", gameWebWithAiMove));
            } else if (endGame == -1) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(Map.of("message", "Тебе нужно больше тренироваться;)", "game", gameWebWithAiMove));
            } else {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(Map.of("message", "А ты хорош:)", "game", gameWebWithAiMove));
            }
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of("message", "null", "game", gameWebWithAiMove));
    }

}
