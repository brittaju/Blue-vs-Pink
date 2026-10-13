package brittaju.domain.service;

import brittaju.domain.model.GamePvE;
import brittaju.web.model.GameWebPvE;

public interface GameServicePvE {

    int[] getBest(GamePvE game);

    boolean isValidationBoard(GamePvE game);

    Integer isGameEnd(GamePvE game);

    GameWebPvE createNewGame();

    GamePvE mapHumanMove(GameWebPvE game);

    GamePvE makeAIMove(GamePvE game);

}
