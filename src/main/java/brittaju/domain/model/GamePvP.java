package brittaju.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class GamePvP {

    public final UUID id;
    public final BoardPvP board;
    private UUID playerXid;
    private UUID playerOid;
    private GameStatus status;
    private LocalDateTime createdAt;

    public GamePvP(BoardPvP board) {
        this.id = UUID.randomUUID();
        this.board = board;
        this.playerXid = null;
        this.playerOid = null;
        this.status = GameStatus.WAITING_FOR_PLAYERS;
        this.createdAt = LocalDateTime.now();
    }
}