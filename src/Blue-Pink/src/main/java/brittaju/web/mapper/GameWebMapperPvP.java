package brittaju.web.mapper;

import brittaju.domain.model.GamePvP;
import brittaju.web.model.GameWebPvP;

public final class GameWebMapperPvP {

    private GameWebMapperPvP() {}

    public static GamePvP toDomain(GameWebPvP game) {
        return new GamePvP(
                game.id(),
                BoardWebMapperPvP.toDomain(game.board()),
                game.playerXid(),
                game.playerOid(),
                game.status(),
                game.createdAt());
    }

    public static GameWebPvP fromDomain(GamePvP game) {
        return new GameWebPvP(
                game.getId(),
                BoardWebMapperPvP.fromDomain(game.getBoard()),
                game.getPlayerXid(),
                game.getPlayerOid(),
                game.getStatus(),
                game.getCreatedAt());
    }

}
