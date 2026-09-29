package brittaju.domain.model;

import java.util.UUID;

public record GamePvE(UUID id, BoardPvE board, boolean isAIGoesFirst) {

    public GamePvE(BoardPvE board) {
        this(UUID.randomUUID(), board, Math.random() < 0.5);
    }

}