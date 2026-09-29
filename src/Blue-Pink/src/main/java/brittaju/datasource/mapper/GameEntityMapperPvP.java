package brittaju.datasource.mapper;

import brittaju.datasource.model.GameEntityPvP;
import brittaju.domain.model.GamePvP;

public final class GameEntityMapperPvP {

    public GameEntityMapperPvP() {}

    public static GamePvP toDomain(GameEntityPvP game) {
        return new GamePvP(
                game.getId(),
                game.getBoard(),
                game.getPlayerXid(),
                game.getPlayerOid(),
                game.getStatus(),
                game.getCreatedAt()
        );
    }

    public static GameEntityPvP fromDomain(GamePvP game) {
        GameEntityPvP e = new GameEntityPvP(
                game.getId(),
                game.getBoard(),
                game.getPlayerXid(),
                game.getPlayerOid(),
                game.getStatus()
        );
        e.setCreatedAt(game.getCreatedAt());
        return e;
    }

}
