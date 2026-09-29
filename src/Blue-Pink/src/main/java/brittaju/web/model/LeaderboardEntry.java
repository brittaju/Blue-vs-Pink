package brittaju.web.model;

import java.util.UUID;

public record LeaderboardEntry(
        UUID userId,
        String login,
        int wins,
        int draws,
        int losses,
        double winRate
) {}