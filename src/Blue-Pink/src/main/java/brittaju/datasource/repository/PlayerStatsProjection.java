package brittaju.datasource.repository;

import java.util.UUID;

public interface PlayerStatsProjection {

    UUID getUserId();

    String getLogin();

    Integer getWins();

    Integer getDraws();

    Integer getLosses();

    Double getWinRate();

}