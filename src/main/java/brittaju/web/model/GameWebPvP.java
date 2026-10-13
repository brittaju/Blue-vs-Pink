package brittaju.web.model;

import brittaju.domain.model.GameStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record GameWebPvP(
        UUID id,
        BoardWeb board,
        UUID playerXid,
        UUID playerOid,
        GameStatus status,
        LocalDateTime createdAt
) {}
