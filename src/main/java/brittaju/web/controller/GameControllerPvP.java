package brittaju.web.controller;

import brittaju.domain.model.GamePvP;
import brittaju.domain.service.GameServicePvP;
import brittaju.web.mapper.GameWebMapperPvP;
import brittaju.web.model.GameWebPvP;
import brittaju.web.model.LeaderboardEntry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/game/pvp")
@RequiredArgsConstructor
@Slf4j
public class GameControllerPvP {

    private final GameServicePvP service;
    private final GameWebMapperPvP webMapperPvP;

    private UUID currentUserId() {
        return (UUID) Objects.requireNonNull(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
        ).getPrincipal();
    }

    @PostMapping("/")
    public ResponseEntity<GameWebPvP> createRoom() {
        GameWebPvP game = service.createNewGame(currentUserId());
        return ResponseEntity.ok(game);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGame(
            @PathVariable UUID id
    ) {
        return service.getById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/available")
    public ResponseEntity<List<GameWebPvP>> listAvailable() {
        return ResponseEntity.ok(service.listAvailable());
    }

    @PostMapping("/join")
    public ResponseEntity<?> join(
            @RequestBody GameWebPvP incomingGame
    ) {
        UUID userId = currentUserId();
        log.info("join called: userId={}, incomingGameId={}", userId, incomingGame.id());
        try {
            GameWebPvP joinedGame = service.joinGame(incomingGame, userId);
            return ResponseEntity.ok(joinedGame);
        } catch (Exception e) {
            log.error("join failed", e);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Присоединиться невозможно: " + e.getMessage());
        }
    }

    @PostMapping("/move")
    public ResponseEntity<?> makeMove(
            @RequestBody GameWebPvP incomingGame
    ) {
        UUID userId = currentUserId();
        GamePvP gameBeforeMove = webMapperPvP.toDomain(incomingGame);
        if (!service.isValidationBoard(gameBeforeMove)) {
            return ResponseEntity
                    .badRequest()
                    .body("Жульничать нехорошо:(");
        }

        GamePvP gameAfterMove = service.makeMove(gameBeforeMove, userId);
        GameWebPvP gameWebAfterMove = webMapperPvP.fromDomain(gameAfterMove);

        return switch (gameAfterMove.getStatus()) {
            case DRAW -> ResponseEntity.ok(Map.of("message", "Ничья!", "game", gameWebAfterMove));
            case PLAYER_X_WON -> ResponseEntity.ok(Map.of("message", "Голубки побеждают!", "game", gameWebAfterMove));
            case PLAYER_O_WON -> ResponseEntity.ok(Map.of("message", "Розочки побеждают!", "game", gameWebAfterMove));
            default -> ResponseEntity.ok(Map.of("message", "null", "game", gameWebAfterMove));
        };
    }

    @GetMapping("/history")
    public ResponseEntity<List<GameWebPvP>> history() {
        UUID userId = currentUserId();
        return ResponseEntity.ok(service.getFinishedGamesByUser(userId));
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<LeaderboardEntry>> leaderboard(
            @RequestParam(name = "n", defaultValue = "10") int n) {
        return ResponseEntity.ok(service.getTopPlayers(n));
    }
}
