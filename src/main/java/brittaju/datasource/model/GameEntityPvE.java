package brittaju.datasource.model;

import brittaju.domain.model.BoardPvE;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
}