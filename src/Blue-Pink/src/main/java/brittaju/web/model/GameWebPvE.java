package brittaju.web.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record GameWebPvE(UUID id, BoardWeb board, boolean isAIGoesFirst) {

    @JsonCreator
    public GameWebPvE(
            @JsonProperty("id") UUID id,
            @JsonProperty("board") BoardWeb board,
            @JsonProperty("isAIGoesFirst") boolean isAIGoesFirst
    ) {
        this.id = id;
        this.board = board;
        this.isAIGoesFirst = isAIGoesFirst;
    }

}
