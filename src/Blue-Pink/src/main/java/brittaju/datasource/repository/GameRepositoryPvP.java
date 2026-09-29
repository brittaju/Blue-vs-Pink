package brittaju.datasource.repository;

import brittaju.datasource.model.GameEntityPvP;
import brittaju.domain.model.GameStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface GameRepositoryPvP extends CrudRepository<GameEntityPvP, UUID> {

    List<GameEntityPvP> findByStatus(GameStatus status);

    @Query("select g from GameEntityPvP g where " +
            "(g.playerXid = :userId or g.playerOid = :userId) " +
            "and g.status in :statuses")
    List<GameEntityPvP> findActiveByUser(@Param("userId") UUID userId,
                                         @Param("statuses") Collection<GameStatus> statuses);

    @Query("""
        select g from GameEntityPvP g
        where (g.playerXid = :userId or g.playerOid = :userId)
          and g.status in (brittaju.domain.model.GameStatus.DRAW,
                           brittaju.domain.model.GameStatus.PLAYER_X_WON,
                           brittaju.domain.model.GameStatus.PLAYER_O_WON)
        order by g.createdAt desc
        """)
    List<GameEntityPvP> findFinishedByUser(@Param("userId") UUID userId);

    @Query(value = """
    WITH results AS (
        SELECT
            g.player_x_id AS user_id,
            CASE WHEN g.game_status = 'PLAYER_X_WON' THEN 1 ELSE 0 END AS won,
            CASE WHEN g.game_status = 'PLAYER_O_WON' THEN 1 ELSE 0 END AS lost,
            CASE WHEN g.game_status = 'DRAW'          THEN 1 ELSE 0 END AS draw
        FROM game_pvp g
        WHERE g.player_x_id IS NOT NULL
          AND g.game_status IN ('PLAYER_X_WON','PLAYER_O_WON','DRAW')

        UNION ALL

        SELECT
            g.player_o_id AS user_id,
            CASE WHEN g.game_status = 'PLAYER_O_WON' THEN 1 ELSE 0 END AS won,
            CASE WHEN g.game_status = 'PLAYER_X_WON' THEN 1 ELSE 0 END AS lost,
            CASE WHEN g.game_status = 'DRAW'          THEN 1 ELSE 0 END AS draw
        FROM game_pvp g
        WHERE g.player_o_id IS NOT NULL
          AND g.game_status IN ('PLAYER_X_WON','PLAYER_O_WON','DRAW')
    )
    SELECT
        r.user_id  AS userId,
        u.login    AS login,
        SUM(r.won)  AS wins,
        SUM(r.draw) AS draws,
        SUM(r.lost) AS losses,
        CASE
            WHEN COUNT(*) = 0 THEN 0
            ELSE SUM(r.won)::float / COUNT(*)
        END AS winRate
    FROM results r
    JOIN users u ON u.uuid = r.user_id
    GROUP BY r.user_id, u.login
    ORDER BY winRate DESC, wins DESC, draws DESC, losses ASC
    LIMIT :limit
    """, nativeQuery = true)
    List<PlayerStatsProjection> findTopPlayers(@Param("limit") int limit);

}
