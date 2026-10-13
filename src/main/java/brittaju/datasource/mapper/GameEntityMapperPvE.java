package brittaju.datasource.mapper;

import brittaju.datasource.model.GameEntityPvE;
import brittaju.domain.model.GamePvE;
import org.springframework.stereotype.Component;

@Component
public final class GameEntityMapperPvE {

    public GamePvE toDomain(GameEntityPvE game) {
        return new GamePvE(
                game.getId(),
                game.getBoard(),
                game.isAIGoesFirst()
        );
    }

    public GameEntityPvE fromDomain(GamePvE game) {
        return new GameEntityPvE(
                game.id(),
                game.board(),
                game.isAIGoesFirst()
        );
    }
}
