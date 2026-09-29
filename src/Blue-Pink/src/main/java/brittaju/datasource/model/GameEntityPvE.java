package brittaju.datasource.model;

import brittaju.domain.model.BoardPvE;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "game_pve")
public class GameEntityPvE {

    @Id
    @Column(name = "game_id", nullable = false, updatable = false)
    private UUID id;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "board", nullable = false)
    private BoardPvE board;

    @Column(name = "is_ai_goes_first", nullable = false)
    private boolean isAIGoesFirst;

    public GameEntityPvE() {}

    public GameEntityPvE(UUID id, BoardPvE board, boolean isAIGoesFirst) {
        this.id = id;
        this.board = board;
        this.isAIGoesFirst = isAIGoesFirst;
    }

    public UUID getId() {
        return id;
    }

    public BoardPvE getBoard() {
        return board;
    }

    public boolean isAIGoesFirst() {
        return isAIGoesFirst;
    }

}