package brittaju.web.mapper;

import brittaju.domain.model.GamePvE;
import brittaju.web.model.GameWebPvE;

public final class GameWebMapperPvE {

    private GameWebMapperPvE() {}

    public static GamePvE toDomainAI(GameWebPvE game) {
        return new GamePvE(game.id(), BoardWebMapperPvE.toDomainAI(game.board()), game.isAIGoesFirst());
    }

    public static GamePvE toDomainHuman(GameWebPvE game) {
        return new GamePvE(game.id(), BoardWebMapperPvE.toDomainHuman(game.board()), game.isAIGoesFirst());
    }

    public static GameWebPvE fromDomainAI(GamePvE game) {
        return new GameWebPvE(game.id(), BoardWebMapperPvE.fromDomainAI(game.board()), game.isAIGoesFirst());
    }

    public static GameWebPvE fromDomainHuman(GamePvE game) {
        return new GameWebPvE(game.id(), BoardWebMapperPvE.fromDomainHuman(game.board()), game.isAIGoesFirst());
    }

}
