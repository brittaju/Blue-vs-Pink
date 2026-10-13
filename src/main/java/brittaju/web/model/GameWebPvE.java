package brittaju.web.model;

import java.util.UUID;

public record GameWebPvE(
        UUID id,
        BoardWeb board,
        boolean isAIGoesFirst
) {}
