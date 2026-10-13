package brittaju.web.mapper;

import brittaju.domain.model.GamePvP;
import brittaju.web.model.GameWebPvP;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class GameWebMapperPvP {

    private final BoardWebMapperPvP boardWebMapperPvP;

    public GamePvP toDomain(GameWebPvP game) {
        return new GamePvP(
                game.id(),
                boardWebMapperPvP.toDomain(game.board()),
                game.playerXid(),
                game.playerOid(),
                game.status(),
                game.createdAt());
    }

    public GameWebPvP fromDomain(GamePvP game) {
        return new GameWebPvP(
                game.getId(),
                boardWebMapperPvP.fromDomain(game.getBoard()),
                game.getPlayerXid(),
                game.getPlayerOid(),
                game.getStatus(),
                game.getCreatedAt());
    }
}
