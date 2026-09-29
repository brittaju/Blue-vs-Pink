package brittaju.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SignUpRequest(
        @JsonProperty("login") String login,
        @JsonProperty("password") String password) {
}
