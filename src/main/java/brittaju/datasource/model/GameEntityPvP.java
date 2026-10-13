package brittaju.datasource.model;

import brittaju.domain.model.BoardPvP;
import brittaju.domain.model.GameStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "game_pvp")
public class GameEntityPvP {

    @Id
    @Column(name = "game_id", nullable = false, updatable = false)
    private UUID id;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "board", nullable = false)
    private BoardPvP board;

    @Column(name = "player_x_id")
    private UUID playerXid;

    @Column(name = "player_o_id")
    private UUID playerOid;

    @Enumerated(EnumType.STRING)
    @Column(name = "game_status", nullable = false)
    private GameStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}