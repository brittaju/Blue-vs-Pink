package brittaju.datasource.mapper;

import brittaju.datasource.model.GameEntityPvE;
import brittaju.domain.model.GamePvE;

public final class GameEntityMapperPvE {

    public GameEntityMapperPvE() {}

    public static GamePvE toDomain(GameEntityPvE game) {
        return new GamePvE(game.getId(), game.getBoard(), game.isAIGoesFirst());
    }

    public static GameEntityPvE fromDomain(GamePvE game) {
        return new GameEntityPvE(game.id(), game.board(), game.isAIGoesFirst());
    }

}
