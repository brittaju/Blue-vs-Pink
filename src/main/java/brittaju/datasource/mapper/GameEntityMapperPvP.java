package brittaju.datasource.mapper;

import brittaju.datasource.model.GameEntityPvP;
import brittaju.domain.model.GamePvP;
import org.springframework.stereotype.Component;

@Component
public final class GameEntityMapperPvP {

    public GamePvP toDomain(GameEntityPvP game) {
        return new GamePvP(
                game.getId(),
                game.getBoard(),
                game.getPlayerXid(),
                game.getPlayerOid(),
                game.getStatus(),
                game.getCreatedAt()
        );
    }

    public GameEntityPvP fromDomain(GamePvP game) {
        return new GameEntityPvP(
                game.getId(),
                game.getBoard(),
                game.getPlayerXid(),
                game.getPlayerOid(),
                game.getStatus(),
                game.getCreatedAt()
        );
    }
}
