package brittaju.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

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

    public GamePvP(UUID id, BoardPvP board, UUID playerXid, UUID playerOid,
                   GameStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.board = board;
        this.playerXid = playerXid;
        this.playerOid = playerOid;
        this.status = status;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public BoardPvP getBoard() {
        return board;
    }

    public UUID getPlayerXid() {
        return playerXid;
    }

    public UUID getPlayerOid() {
        return playerOid;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setPlayerXid(UUID playerXid) {
        this.playerXid = playerXid;
    }

    public void setPlayerOid(UUID playerOid) {
        this.playerOid = playerOid;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

}