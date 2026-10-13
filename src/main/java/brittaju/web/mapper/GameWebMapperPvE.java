package brittaju.web.mapper;

import brittaju.domain.model.GamePvE;
import brittaju.web.model.GameWebPvE;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class GameWebMapperPvE {

    private final BoardWebMapperPvE boardWebMapperPvE;

    public GamePvE toDomainAI(GameWebPvE game) {
        return new GamePvE(
                game.id(),
                boardWebMapperPvE.toDomainAI(game.board()),
                game.isAIGoesFirst()
        );
    }

    public GamePvE toDomainHuman(GameWebPvE game) {
        return new GamePvE(
                game.id(),
                boardWebMapperPvE.toDomainHuman(game.board()),
                game.isAIGoesFirst()
        );
    }

    public GameWebPvE fromDomainAI(GamePvE game) {
        return new GameWebPvE(
                game.id(),
                boardWebMapperPvE.fromDomainAI(game.board()),
                game.isAIGoesFirst()
        );
    }

    public GameWebPvE fromDomainHuman(GamePvE game) {
        return new GameWebPvE(
                game.id(),
                boardWebMapperPvE.fromDomainHuman(game.board()),
                game.isAIGoesFirst()
        );
    }
}
